## MSRA_proposal_wifi_log

#### 이 문제에 대한 생각
	• 현재 이용하는 데이터셋
		○ 코오롱 Wi-Fi 데이터셋 
			§ feature로 쓸 만한 게 너무 없음. 어디에 Wi-Fi가 찍혔느냐랑 revisit count, dwell time, revisit period밖에 없음.
			§ 교수님이 생각하는 문제에 필요한 feature를 갖고 있긴 함.
	• 기타 데이터셋
		○ 스마트 워치 데이터셋 (2시에 이의진 교수님과 미팅하기로 함)
		○ 민수형이 알려줬던 데이터셋
		○ Kaggle data (이건 목적이 revisit intention이 아님 - movement pattern등을 비롯한 로그(사용 앱, 앱 카테고리, 위도 경도, 폰 기종)를 이용하여 남/녀, 나이대 category를 맞추는 게 문제)
	• Feature를 잡으면 당연히 더 나아질 텐데, model를 만드는 게 아니라 feature engineering 문제라 old한 느낌.


#### 금주 일정
	• 월: 엑소브레인 최종 통합
		○ 솔트룩스 서버에 RDF-Unit, B-Box 설치 및 쉘 스크립트 작성, 작동 및 데이터 확인 완료
	• 화: MSRA 프로포절 데이터 분석 시작: 코오롱 781번 매장에 대한 크롤링
		○ Repo: https://github.com/Seondong/MSRA_proposal_wifi_log
		○ 언어: Python + Jupyter notebook
		○ 데이터 정보
			§ 크롤링 데이터 기간
				□ 크롤링 시작 ts: 1471870576 (08/22/2016 @12:56pm)
				□ 크롤링 완료 ts: 1440568069 (08/26/2015 @ 5:47am)
			§ Statistics
				□ 로그 수: 3,962,136개 (DF를 Pickle로 저장: 430.2MB)
				□ Unique device IDs: 1,526,073개
	• 수: 데이터 프레임 분석, revisit count에 대한 sup. learning 모델 만들기
		○ xgboost(end-to-end gradient boosting lib), keras(dnn) 이용
		○ 참고: https://www.kaggle.com/c/talkingdata-mobile-user-demographics
			§ 최근 Kaggle에 Kernel/Notebook 공유가 활발해서, 다른 데이터 분석가들의 코드를 참고하기 좋음(주로 R, Python).
		○ Sup. learn. model
			§ 일단 raw data를 feature로 이용
		○ 평가 방법 결정
			§ Log-loss (sci-kit learn)
	• 목: 추가 데이터 크롤링, stay-point을 define하고 그에 맞는 feature 생성
	• 금:  미팅 준비

#### 추후 일정
	• 8/29 - 9/2:
		
	• 9/5 - 9/9:

