# adtool

* 🔗링크 : http://adtool.kr 


* 🔊 프로젝트 소개
  * 동남아시아 쇼핑몰의 상품 리뷰를 위한 체험단을 모집하고 관리 하기 위한 폐쇄형 웹서비스 프로젝트입니다.
  * 현재 약 500여명의 국내 및 동남아시아의 회원들이 이용중입니다.
  * 첫 회사에 입사 후 2개월차에 처음으로 맡은 프로젝트입니다.
  * 디자인과 퍼블리싱을 제외한 DB설계, 백엔드, 프론트엔드까지 모두 혼자 작업하였습니다.
  * 관리자는 체험단을 모집하기 위해 캠페인(상품)을 생성하고 체험단에 신청한 회원을 관리합니다.
  * 사용자는 생성된 캠페인을 선택하고 체험단 신청을 합니다.


* 🏗️개발 기간 및 인원 
  * 2021.10 ~ 2022.01 (2023.02까지 유지보수)
  * 풀스택 1명, 디자인 퍼블리셔 1명 총 2명 중 풀스택 담당

* 🛠️사용 기술
  * Back-End: Spring Boot, Java, MyBatis, MariaDB
  * Front-End: JavaScript, jQuery

* 📅 DB
  * 테이블 정의서 : https://docs.google.com/document/d/1st21qukFUlUPDubiymRQGz0GIPatiGZo/edit
  * ERD
  ![adtool_erd](https://github.com/maroowj/adtool/assets/77284101/4409f80d-6cb2-4f51-a377-111de67acf30)

* 💡부가기능
  * Spring-MVC 패턴
  * Spring-mail-start 라이브러리를 사용하여 메일 보내기 기능 자체 구현 (비밀번호 찾기 기능)
  * Spring-Batch, Quartz 라이브러리를 이용한 캠페인 예약 발행 기능 구현
  * apach-poi, commons-io 라이브러리를 사용한 테이블 엑셀 다운로드 기능 구현
  * session 및 쿠키를 활용한 로그인 및 로그인 저장 기능 구현

* ✏️ 시나리오
  * 관리자
    * 리뷰 체험단을 모집하려는 아이템의 캠페인을 발행합니다. (예약 또는 즉시 발행)
    * 모집이 완료된 체험단을 관리합니다. (승인 또는 거절)
    * 체험단이 상품 리뷰를 완료하고 첨부한 증빙자료를 확인한다.
    * 증빙자료 확인 후 정산 내역을 기록한다.
    * 정산이 모두 완료된 캠페인은 자동적으로 종료된다.
    * 이외 전체회원 관리, 메시지 전송, 공지사항 및 게시물을 등록 또는 수정 할 수 있다.
  * 사용자
    * 회원가입을 한다. (기본정보 입력 - 관심사 입력 - SNS계정 입력 총3단계)
    * 로그인 후 모집중인 캠페인에 참가 신청을 한다. (본인 국가와 일치해야 하며, 인원 수 제한, 중복 신청 방지)
    * 체험단 신청이 승인되면 제품 구매 후 체험 리뷰를 작성한다.
    * 마이페이지의 참여중인 캠페인 페이지에서 리뷰 증빙자료와, 영수증을 첨부하여 업로드 한다.
    * 이외 관리자로부터 개별적으로 송신된 메시지를 확인하고 개인정보를 수정할 수 있다.

   
* 💻구동 화면
  * 관리자
![admin_index](https://github.com/maroowj/adtool/assets/77284101/9598532f-0526-4b9e-a8c3-b470c3351108)
![admin_campaign_insert](https://github.com/maroowj/adtool/assets/77284101/b24226f6-d7a7-4e99-885c-3316200c82c0)
![admin_joiner_list](https://github.com/maroowj/adtool/assets/77284101/272b7be4-4971-4551-a2c7-b4cf6551662b)

  * 사용자
![user_index](https://github.com/maroowj/adtool/assets/77284101/e2436f8d-2915-4a92-a006-2b83db48df96)
![campaign_details](https://github.com/maroowj/adtool/assets/77284101/e3d22061-848d-489b-88de-b5be73a87e7c)
![user_forgot_password](https://github.com/maroowj/adtool/assets/77284101/e1063202-2de5-4526-98b4-825abc82b7cf)
![user_login](https://github.com/maroowj/adtool/assets/77284101/80cbe0f9-2028-4605-bcb9-4eb82bd807c1)
![user_my_page](https://github.com/maroowj/adtool/assets/77284101/0c60cb3c-0aa8-4ddc-af3f-04452d6105fb)
![user_joining_campaign](https://github.com/maroowj/adtool/assets/77284101/101a91f1-d982-4644-96a9-e66136e3a8c4)
  

