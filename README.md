# K-WASM(K-WebAssembly)

K-WASM은 K-PaaS 클라우드 컴퓨팅 환경에 적용 가능한 WebAssembly으로 C언어, C++, RUST, 자바 등 다양한 컴파일 프로그래밍언어들을 컴파일하여 웹 브라우저에 실행 가능한 결과물을 제공하는 오픈소스 소프트웨어를 목표로 합니다.
 
K-PaaS는 해외 벤더 중심의 클라우드 플랫폼 시장에서 국내 IT 서비스 경쟁력 강화를 목표로, 한국지능정보사회진흥원(NIA)의 지원으로 다양한 국내 업체와 협업을 통해 만든 개방형 클라우드 플랫폼입니다.
 - K-PaaS 공식 사이트 : https://k-paas.or.kr/

현재 릴리즈된 K-WASM은 C언어를 기반으로 WebAssembly 가능하도록 기능을 구현하였으며, 지속적인 연구 개발을 통해 C++, RUST 등 다양한 컴파일 프로그래밍언어들을 제공하도록 하겠습니다.

## K-WASM 개발 환경

K-WASM을 구현하기 위해 사용된 개발 환경으로 실행 환경 구현에 참고하길 바랍니다.

| 항목 | 내용                                    |
| -------             | -------------------------------------------- |
| 언어                | 자바 17(OpenJDK 가능)
| 공통 주요 프레임워크  |  Spring Boot Frameworks 3.16 |
| 개발도구    |  IntelliJ |
| 빌드도구    |  Gradle 8.5 |
| 인프라 환경    |  K-PaaS 인프라 적용(Ubuntu 22.04, CentOS 8, Rocky Linux 8) |
| Docker 버전    |  4.26.1 정상동작 확인(테스트 결과) |

## 시스템 기본 구성
![image](https://github.com/K-PaaS-incubator/k-wasm-app/assets/39357722/e1f630a6-266f-4c0c-b3b9-55082c1d629f)


## 구현 앱

K-WASM은 클라우드 네이티브 어플리케이션 기반으로 구현하는 것을 목표로 MSA 아키텍처에 적합하도록 다양한 앱들을 구현하였습니다.

### 1. app-manager
 - MSA 아키텍처 적용에 따른 다양한 앱을 관리하는 UI 앱 서비스
 - 적용 프레임워크
   * Spring Boot Admin
 - 도커 이미지 생성 파일 및 도커 버전 : Dockerfile / version 4.26.1
 - 도커 이미지 파일 다운로드 링크 : https://hub.docker.com/repository/docker/leehyeopgeon/app-manager/general
     
### 2. kwasm-config
 - 앱에 적용된 환경 설정 분리를 위해 환경설정 파일들이 저장되는 저장소
 - kwasm-config 앱에 제공되는 환경설정 저장소
       
### 3. config-server
 - 다양한 앱에 적용될 환경설정들을 제공하기 위해 Spring Cloud Config Server를 적용한 Config Server
 - kwasm-config 디렉토리에 저장된 환경설정들을 K-WASM 앱에 제공
 - 적용 프레임워크
   * Spring Cloud Config Server
   * Spring Boot Web
 - 도커 이미지 생성 파일 및 도커 버전 : Dockerfile / version 4.26.1
 - 도커 이미지 파일 다운로드 링크 : https://hub.docker.com/repository/docker/leehyeopgeon/config-server/general

### 4. eureka-server
 -  서비스 디스커버리 등 서비스 관리를 위해 구현
 - 적용 프레임워크 :
    * Spring Cloud Netflix Eureka Server
    * Spring Boot Actuator
 - 도커 이미지 생성 파일 및 도커 버전 : Dockerfile / version 4.26.1
 - 도커 이미지 파일 다운로드 링크 : https://hub.docker.com/repository/docker/leehyeopgeon/eureka-server/general
   
### 5. ui-html
 - HTML 기반 K-WASM앱 UI
 - 추후 React 등 다양한 프론트앤드 기술들을 활용한 UI 샘플 앱들을 제공할 예정
 - 도커 이미지 생성 파일 및 도커 버전 : Dockerfile / version 4.26.1
 - 도커 이미지 파일 다운로드 링크 : https://hub.docker.com/repository/docker/leehyeopgeon/ui-html/general

### 6. api-gateway
 - MSA 아키텍처 구현을 위한 API Gateway
 - 적용 프레임워크
   * Reactive 기반 Spring Cloud Gateway
   * Spring Boot Actuator
   * Spring Boot Admin Client
   * Spring Cloud Config Client
   * Spring Cloud Netflix Eureka Client

### 7. wasm-manager
 - K-WASM 메타 데이터 생성 및 관리, WASM Compiler 연계
 - 적용 프레임워크
   * Spring Boot Web
   * Spring Boot Actuator
   * Spring Boot Admin Client
   * Spring Data JPA
   * Spring Cloud Config Client
   * Spring Cloud Netflix Eureka Client
          
### 8. db-scheme
 - K-WASM Manager에서 관리하는 메타정보 생성을 위한 SQL

## K-WASM 샘플 및 실행결과

### 샘플 파일(컴파일언어)
 - test-data 폴더
 - WASM 컴파일 테스트하기 위한 프로그래밍언어 파일
   
### 샘플 실행결과
 - kwasm-result-sample 폴더
 - test-data의 샘플 C언어를 K-WASM으로 컴파일한 결과 샘플

### 샘플 테스트 실행 앱
 . kwasm-sample-web 폴더
 - K-WASM 컴파일 결과 파일들이 일반 웹프로젝트에 정상적으로 동작되는지 테스트한 샘플 앱
 - kwasm-result-sample 결과를 기반으로 스프링 부트 웹에 테스트 진행
   

## 실행방법
1번부터 순서대로 앱을 배포 및 실행하길 권장함

### 1. DB 배포
 -  MariaDB or MySQL DB 및 테이블 생성(ORM을 사용하기에 테이블은 자동 생성)
 -  테이블 생성 SQL은 db-scheme 디렉토리 참고
  
### 2. config-server 실행
   - 환경설정 변경은 application.yml 수정
  
### 3. eureka-server 실행

### 4. app-manager 실행

### 5. wasm-manager 실행

### 6. api-gateway 실행

### 7. ui-html 실행

## 실행화면

### 1. UI 서비스 실행화면
* [파일가져오기] : C언어 파일의 읽어 내용을 출력함
* [실행하기] : K-WASM 컴파일 실행
* [WASM 다운로드] : K-WASM 컴파일 실행 결과 파일들(.js, .wasm, .html) ZIP으로 압축하여 다운로드
  
![image](https://github.com/K-PaaS-incubator/k-wasm-app/assets/39357722/0b1a4b8a-be9d-489c-ac0c-9fcc0c9661dc)

### 2. K-WASM 컴파일 결과 다운로드

![image](https://github.com/K-PaaS-incubator/k-wasm-app/assets/39357722/dfacf633-4fdd-402e-b0c5-d20c7f5a8c87)

### 예) HelloWorld.c 컴파일 결과 파일들
![image](https://github.com/K-PaaS-incubator/k-wasm-app/assets/39357722/3d8e2d69-e3ce-403c-a187-06c7e717c683)


## 제공서비스

### 1. 웹 서비스
* https://k-wasm.kr/wasm/k-wasm.html

### 2. API 서비스
* https://api.k-wasm.kr 추후 오픈 예정

## K-WASM으로 구현가능한 서비스 예

#### 인공지능 플랫폼인 OpenCV와 C++언어로 WASM 컴파일한 얼굴인식 데모
* https://sample.k-wasm.kr/html/example1/face_detection.html

#### 인공지능 플랫폼인 OpenCV와 C++언어로 WASM 컴파일한 영상필터링 데모
* https://sample.k-wasm.kr/html/example2/cv_scale.html

* #### C언어 함수를 웹에서 실행하는 WASM 데모
* https://sample.k-wasm.kr/html/example3/sample.html
  
## 라이센스

누구나 사용가능한 오픈소스를 지향하며, Apache-2.0 license를 따릅니다.

