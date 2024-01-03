# k-wasm-app

K-PaaS는 해외 벤더 중심의 클라우드 플랫폼 시장에서 국내 IT 서비스 경쟁력 강화를 목표로, 한국지능정보사회진흥원(NIA)의 지원으로 다양한 국내 업체와 협업을 통해 만든 개방형 클라우드 플랫폼입니다.
 - K-PaaS 공식 사이트 : https://k-paas.or.kr/

K-WASM은 앞서 설명한 K-PaaS에 적용가능한 WebAssembly로 C언어, C++, RUST, 자바 등 다양한 컴파일을 언어를 컴파일할 수 있는 오픈소스 소프트웨어로 제공하는 것을 목표로 합니다.

현재 릴리즈된 K-WASM은 C언어를 기반으로 WebAssembly 가능하도록 기능 구현하였으며, 지속적인 연구 개발을 통해 C++, RUST 등 다양한 컴파일을 언어를 제공하도록 하겠습니다.

<개발환경>
| 항목 | 내용                                    |
| -------             | -------------------------------------------- |
| 언어                | 자바 17(OpenJDK 가능)
| 공통 주요 프레임워크  |  Spring Boot Frameworks 3.16 |
| 개발도구    |  IntelliJ |
| 빌드도구    |  Gradle 8.5 |
| 인프라 환경    |  K-PaaS 인프라 적용(Ubuntu 22.04, CentOS 8, Rocky Linux 8) |
| Docker 버전    |  4.26.1 정상동작 확인(테스트 결과) |

<구현 앱>
1. app-manager
 - MSA 아키텍처 적용에 따른 다양한 앱을 관리하는 UI 앱 서비스
 - 적용 프레임워크 : Spring Boot Admin
 - 도커 이미지 생성 파일 및 도커 버전 : Dockerfile / version 4.26.1
 - 도커 이미지 파일 다운로드 링크 : https://hub.docker.com/repository/docker/leehyeopgeon/app-manager/general
     
2. kwasm-config
 - 앱에 적용된 환경 설정 분리를 위해 환경설정 파일들이 저장되는 저장소
 - 적용 프레임워크 : 없음
      
3. config-server
 - 환경 설정을 위해 Spring Cloud Config Server를 적용한 Config Server
 - 적용 프레임워크
     * Spring Cloud Config Server
     * Spring Boot Web
 - 도커 이미지 생성 파일 및 도커 버전 : Dockerfile / version 4.26.1
 - 도커 이미지 파일 다운로드 링크 : https://hub.docker.com/repository/docker/leehyeopgeon/config-server/general

4. eureka-server
 -  서비스 디스커버리 등 서비스 관리를 위해 구현
 - 적용 프레임워크 :
    * Spring Cloud Netflix Eureka Server
    * Spring Boot Actuator
 - 도커 이미지 생성 파일 및 도커 버전 : Dockerfile / version 4.26.1
 - 도커 이미지 파일 다운로드 링크 : https://hub.docker.com/repository/docker/leehyeopgeon/eureka-server/general
   
5. ui-html
 - HTML 기반 K-WASM앱 UI
 - 도커 이미지 생성 파일 및 도커 버전 : Dockerfile / version 4.26.1
 - 도커 이미지 파일 다운로드 링크 : https://hub.docker.com/repository/docker/leehyeopgeon/ui-html/general

6. api-gateway
 - MSA 아키텍처 구현을 위해 API-GATEWAAY 적용
 - 적용 프레임워크
   * Reactive 기반 Spring Cloud Gateway
   * Spring Boot Actuator
   * Spring Boot Admin Client
   * Spring Cloud Config Client
   * Spring Cloud Netflix Eureka Client

7. wasm-manager
 - K-WASM 메타 데이터 생성 및 관리
 - 적용 프레임워크
   * Spring Boot Web
   * Spring Boot Actuator
   * Spring Boot Admin Client
   * Spring Data JPA
   * Spring Cloud Config Client
   * Spring Cloud Netflix Eureka Client
          
8. test-data
 - WASM 컴파일 테스트하기 위한 프로그래밍언어 파일 

9. db-scheme
 - K-WASM Manager에서 관리하는 메타정보 생성을 위한 SQL

10. kwasm-result-sample
 - test-data의 샘플 C언어를 K-WASM으로 컴파일한 결과 샘플

11. kwasm-sample-web
 - K-WASM 컴파일 결과 파일들이 일반 웹프로젝트에 정상적으로 동작되는지 테스트한 샘플 앱
 - kwasm-result-sample 결과를 기반으로 스프링 부트 웹에 테스트 진행
