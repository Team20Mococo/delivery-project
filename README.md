# delivery-project

# readme

## 팀원 역할분배

다원 - 스토어, 상품, AI 도메인

재구 - 주문, 배달 결재 도메인

태환 - 유저 도메인, 인증/인가, Global Handler, Custom Exception

## 서비스 구성 및 실행 방법

application-secret.yml 파일에서

redis host = 로컬 레디스 서버

username, password에 맞게 설정 후 실행

## 프로젝트 목적/ 상세

온/오프라인 주문 서비스를 만들어 보자.

단, 추후 서비스 이용자 규모에 따라 확장 및  각 서비스 단위 어플리케이션으로 분리(MSA)가 용이하도록 설계

## ERD
![erd](./mococo_order(4).png)
## 기술 스택

Language : Java 17

Framework : SpringBoot 3.3

DataBase : PostgreSQL, Redis

Library : Spring Data JPA, JWT
