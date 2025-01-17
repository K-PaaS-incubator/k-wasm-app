# 🌐 K-WASM (K-WebAssembly)

**K-WASM**은 K-PaaS 클라우드 컴퓨팅 환경에 최적화된 WebAssembly 기반 오픈소스 프로젝트입니다. C, C++, Rust, Java 등 다양한 언어를 컴파일하여 웹 브라우저에서 실행 가능한 결과물을 제공합니다.

---

## 🚀 프로젝트 개요

### **K-PaaS 소개**
- **K-PaaS**는 국내 IT 서비스 경쟁력 강화를 목표로 한국지능정보사회진흥원(NIA) 지원을 받아 개발된 개방형 클라우드 플랫폼입니다.
- [K-PaaS 공식 사이트](https://k-paas.or.kr/)

### **K-WASM 주요 목표**
- C언어 기반 WebAssembly 구현 완료.
- 추후 C++, Rust 등 다양한 언어 지원 예정.

---

## 🛠️ 개발 환경

| 항목                   | 내용                                   |
|------------------------|---------------------------------------|
| **언어**              | Java 17 (OpenJDK 호환 가능)            |
| **주요 프레임워크**     | Spring Boot 3.16                      |
| **개발 도구**          | IntelliJ IDEA                         |
| **빌드 도구**          | Gradle 8.5                            |
| **인프라 환경**         | K-PaaS (Ubuntu 22.04, CentOS 8 등)     |
| **Docker 버전**        | 4.26.1 (테스트 완료)                   |

---

## 📦 주요 구성 요소

### **1. app-manager**
- MSA 아키텍처 기반 앱 관리 UI 서비스.
- **프레임워크:** Spring Boot Admin.
- **도커 이미지:** [Docker Hub 링크](https://hub.docker.com/repository/docker/leehyeopgeon/app-manager/general).

### **2. kwasm-config**
- 환경설정 파일 저장소.

### **3. config-server**
- Spring Cloud Config Server 기반 환경설정 제공 서비스.
- **도커 이미지:** [Docker Hub 링크](https://hub.docker.com/repository/docker/leehyeopgeon/config-server/general).

### **4. eureka-server**
- 서비스 디스커버리 기능.
- **프레임워크:** Spring Cloud Netflix Eureka Server.
- **도커 이미지:** [Docker Hub 링크](https://hub.docker.com/repository/docker/leehyeopgeon/eureka-server/general).

### **5. ui-html**
- HTML 기반 UI 샘플 앱.
- **도커 이미지:** [Docker Hub 링크](https://hub.docker.com/repository/docker/leehyeopgeon/ui-html/general).

### **6. api-gateway**
- MSA 구현용 API Gateway.
- **프레임워크:** Spring Cloud Gateway (Reactive 기반).

### **7. wasm-manager**
- K-WASM 메타데이터 관리 및 WASM 컴파일러 연계.
- **프레임워크:** Spring Data JPA, Spring Boot Actuator.

---

## 🧩 실행 방법

1. **DB 설정**
   - MariaDB/MySQL 사용.
   - `db-scheme` 디렉토리 참고하여 테이블 생성.

2. **서비스 실행 순서**
   1. `config-server` 실행.
   2. `eureka-server` 실행.
   3. `app-manager` 실행.
   4. `wasm-manager` 실행.
   5. `api-gateway` 실행.
   6. `ui-html` 실행.

---

## 🎨 실행 화면

### **UI 서비스**
- [파일 가져오기], [실행하기], [WASM 다운로드] 기능 제공.
![UI 실행 화면](https://github.com/K-PaaS-incubator/k-wasm-app/assets/39357722/0b1a4b8a-be9d-489c-ac0c-9fcc0c9661dc)

### **WASM 컴파일 결과**
- 예) `HelloWorld.c` 컴파일 결과.
![컴파일 결과](https://github.com/K-PaaS-incubator/k-wasm-app/assets/39357722/3d8e2d69-e3ce-403c-a187-06c7e717c683)

---

## 🌟 제공 서비스

| 서비스 유형 | URL                              |
|-------------|----------------------------------|
| **웹 서비스**  | [https://k-wasm.kr/wasm/k-wasm.html](https://k-wasm.kr/wasm/k-wasm.html) |
| **API 서비스** | [https://api.k-wasm.kr](https://api.k-wasm.kr) (추후 오픈) |

---

## 🧪 샘플 프로젝트

### **샘플 코드**
- `test-data/`: C언어 샘플 파일.

### **테스트 앱**
- `kwasm-sample-web`: K-WASM 컴파일 결과 테스트용 웹 프로젝트.

---

## 📖 라이선스

Apache-2.0 License를 따르는 오픈소스 프로젝트입니다.  
[LICENSE 파일](LICENSE)에서 자세한 내용을 확인하세요.

---

**💡 문의 사항 및 기여**  
프로젝트에 대한 문의는 [이슈](https://github.com/K-PaaS-incubator/k-wasm-app/issues)에 등록해주세요!
