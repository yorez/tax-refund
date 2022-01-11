# API 구축 과제 - 지원자 김영준

# API 정리
## 기술
- 과제 필수 요구사항인 java, spring-boot, jpa, h2, gradle을 구현에 사용하였습니다.
- 인증부는 spring security + jwt를 db 스키마 관리는 liquibase를 추가로 사용하였습니다.
- h2는 http://localhost:8080/h2-console 에서 id: young-jun / password:공란 으로 확인가능합니다.
- swagger-ui는 http://localhost:8080/swagger-ui/ 에서 확인 가능합니다.

## 1-1. 회윈가입 API
- path: /szs/signup
- param: userId(String), password(String), name(String), regNo(String)
- 패스워드는 BCryptPasswordEncoder를 사용하여 단방향 암호화 처리하였습니다.
- 주민등록번호는 AES256알고리즘을 사용하여 양방향 암호화 처리하였습니다.
- 주민등록번호는 정규성검증하여 정해진 패턴만 입력가능하도록 처리하였습니다.

## 1-2. 로그인 API
- path: /szs/login
- param: userId(String), password(String)
- 인증에는 스프링 시큐리티를 사용하였고 jwt filter를 추가하였습니다.
- 로그인 성공시 jwt token을 발급하고 해당 토큰의 유효성을 signup, login을 제외한 모든 api경로에서 확인하도록 구성하였습니다.

## 1-3. 내 정보보기 API
- path: /szs/me
- param: x
- 인증 토큰을 이용하여 로그인한 본인 정보만 확인하도록 구성하였습니다.

## 2. 유저 정보 스크랩 API
- path: /szs/scrap
- param: x
- 인증 토큰을 이용하여 로그인한 본인 정보만 확인하도록 구성하였습니다.
- 스크랩 정보를 저장하기 위해 tbl_scrap, tbl_scrap_salary(scrap001), tbl_scrap_tax(scrap002) 로 테이블을 구성하였습니다.
- JPA 연관 관계 ScrapSalary to Scrap(N:1), ScrapTax to Scrap(N:1)를 각각 형성하였습니다.
- database에 스크랩한 정보가 있으면 바로 응답, 아니면 스크랩 api를 호출하여 응답하도록 구성하였습니다.
- 최초 회원 가입시 스크랩URL을 별도 스레드에서 1회 호출하도록 구성하였습니다.
- 매일 새벽1시에 회원가입정보에는 존재하나 로컬DB에 스크랩 정보가 없는 사용자를 대상으로 스크랩URL 호출 및 저장하도록 구성하였습니다.

### 스크랩 URL 개선점
- json 객체 필드들의 타입이 일정치 않아서(ex> 같은 필드가 사용자에 따라 문자 또는 숫자로 넘어옴) 타입 캐스팅 부가 추가 되었습니다. 최종 응답 전에 타입 캐스팅이 되면 좋을 것 같습니다.

## 3. 환급액 계산 API
- path: /szs/refund
- param: x
- 인증 토큰을 이용하여 본인 정보만 확인하도록 구성하였습니다.
- database에 스크랩한 정보가 있으면 바로 환급액 계산 결과를 응답, 아니면 스크랩 api를 호출 >> 환급액 계산하여 응답하도록 구성하였습니다.

# 주관식 과제

1. 테스트코드 작성시 setup 해야 할 데이터가 대용량이라면 어떤 방식으로 테스트코드를 작성하실지 서술해 주세요.

- 대용량 데이터 연동부를 대역으로 대처하는 테스트 코드를 작성하겠습니다. 
- 스텁(or모의) 객체를 사용해서 데이터 호출결과에 대한 응답값을 대체 하도록 하겠습니다.
- 스파이(or모의) 객체를 사용해서 데이터에 대한 테스트가 아닌 행위에 대한 테스트를 구성하도록 하겠습니다.

2. 이벤트 드리븐 기반으로 서비스를 만들 때 이벤트를 구성하는 방식과 실패 처리하는 방식에 대해 서술해 주세요.
 
3. MSA 구성하는 방식에는 어떤 것들이 있고, 그중 선택하신다면 어떤 방식을 선택하실 건가요?

4. 외부 의존성이 높은 서비스를 만들 때 고려해야 할 사항이 무엇인지 서술해 주세요.
- 라이브러리에 대한 학습테스트들을 구성해서 버전 변경이 발생하더라도 기능들이 동일하게 지원되는지 확인 할 수 있어야 합니다. 
- 라이센스, 구매 비용, 상업 배포 가능 여부, 추후유지보수 지원 등을 사전에 확인해야합니다. 
 
5. 일정이 촉박한 프로젝트를 진행하게 되었습니다. 이 경우 본인의 평소 습관에 맞춰 개발을 진행할 지, 회사의 코드 컨벤션에 맞춰 개발할지 선택해 주세요. 그리고 그 이유를 서술해 주세요.
- 코드 컨벤션에 맞춰서 개발 하겠습니다. 여러명이 동시에 프로젝트를 진행한다는 전제하에 특정 기준없이 각자 스타일대로 개발하면 향후 유지보수성이 떨어질 것이기 때문 입니다. 
또 급한 상황에서 컨벤션을 갑자기 바꾼다면 예상치 못한 부분에서 기존에 유지하고 있던 알고리즘들이 깨질 수 있기 때문입니다.
 
6. 민감정보 암호화 알고리즘에는 어떤 것들이 있고, 그중 선택하신다면 어떤 것을 선택하실 건가 요? 그 이유는 무엇인가요?
- 암복호화가 가능하고 속도가 우수한 대칭키 알고리즘을 선택하겠습니다. 한국인터넷 진흥원(kisa)에서 권고하는 안전한 대칭키 알고리즘 AES, SEED, HIGHT, ARIA, LEA 등에서 택1 할 수 있는데, 
소프트웨어로 구현이 용이한 AES를 선택하도록 하겠습니다.
> 참조: [암호 알고리즘 및 키 길이 이용 안내서(2018.12)])(https://seed.kisa.or.kr/kisa/Board/38/detailView.do)

