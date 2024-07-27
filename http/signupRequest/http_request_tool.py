import requests
import argparse
from concurrent.futures import ThreadPoolExecutor, as_completed
import logging
import time
import sys

# 로깅 설정
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

# API 엔드포인트 설정
SIGNUP_URL = "http://host.docker.internal:8081/api/members/signup"

# 요청 타임아웃 설정 (초 단위)
REQUEST_TIMEOUT = 15

# 배치 처리 설정
BATCH_SIZE = 100
BATCH_DELAY = 5  # 배치 사이의 지연 시간 (초)

# 고유한 사용자 정보 생성 함수
def generate_member_info(member_id, batch_number):
    return {
        "name": f"member_{member_id}",
        "email": f"member_{member_id}@example.com",
        "password": "123123",
        "phone": f"010-1234-5678",
        "address": f"Some Address {member_id}"
    }

# 회원가입 함수
def register_member(member_info):
    try:
        response = requests.post(SIGNUP_URL, json=member_info, timeout=REQUEST_TIMEOUT)
        if response.status_code in [200, 201]:
            logging.info(f"Registration successful for {member_info['email']}")
            return True
        else:
            logging.warning(f"Registration failed for {member_info['email']}: {response.status_code}")
            return False
    except requests.exceptions.RequestException as e:
        logging.error(f"Registration error for {member_info['email']}: {e}")
        return False

# 메인 함수
def main(skip_registration):
    member_ids = range(1, 10001)  # 예시로 10000명의 사용자를 생성

    with ThreadPoolExecutor(max_workers=10) as executor:
        futures = []
        for member_id in member_ids:
            batch_number = (member_id - 1) // BATCH_SIZE + 1
            member_info = generate_member_info(member_id, batch_number)
            if not skip_registration:
                futures.append(executor.submit(register_member, member_info))

            # 배치 단위로 딜레이 적용
            if len(futures) % BATCH_SIZE == 0:
                for future in as_completed(futures):
                    future.result()
                futures = []
                time.sleep(BATCH_DELAY)

        # 남은 작업 처리
        for future in as_completed(futures):
            future.result()

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Automate member registration process.")
    parser.add_argument('--skip_registration', action='store_true', help='Skip the registration process if members are already registered')
    args = parser.parse_args()

    main(args.skip_registration)