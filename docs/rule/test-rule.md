# 테스트 작성 가이드

## 목차
1. [테스트 작성 원칙](#테스트-작성-원칙)
2. [테스트 구조 및 명명 규칙](#테스트-구조-및-명명-규칙)
3. [테스트 유형별 작성 방법](#테스트-유형별-작성-방법)
4. [테스트 데이터 준비](#테스트-데이터-준비)
5. [의존성 처리](#의존성-처리)
6. [동시성 테스트](#동시성-테스트)
7. [어노테이션 사용법](#어노테이션-사용법)

## 테스트 작성 원칙

### 1. Given-When-Then 패턴 준수
모든 테스트는 Given-When-Then 구조를 따릅니다.

```java
@Test
@DisplayName("[성공] 계좌의 잔액이 주어진 금액보다 크면 출금이 가능하다")
void canWithdraw() {
    // given:
    final Account account = AccountFixture.anAccount()
            .withBalance(Money.wons(5_000))
            .build();

    // when:
    final boolean canWithdraw = account.canWithdraw(Money.wons(5_000));

    // then:
    assertTrue(canWithdraw);
}
```

### 2. 테스트 메서드명 규칙
- 메서드명은 영어로 작성하되, 한글 설명을 @DisplayName에 작성
- 테스트 대상 메서드명_상황_예상결과 형태로 작성
- 성공/실패 케이스를 명확히 구분

```java
@Test
@DisplayName("[실패] 계좌의 잔액이 주여진 금액보다 작으면 출금이 불가능하다")
void canWithdraw_fail() {
    // 테스트 구현
}
```

### 3. 변수 선언 규칙
- final 키워드 사용을 권장
- 의미있는 변수명 사용
- 매직 넘버 대신 상수나 명시적 값 사용

## 테스트 구조 및 명명 규칙

### 패키지 구조
```
src/test/java/
└── tally/transfer/
    ├── account/
    │   ├── domain/          # 도메인 로직 테스트
    │   ├── application/     # 애플리케이션 서비스 테스트
    │   └── stub/           # 테스트용 구현체
    ├── common/
    │   ├── concurrent/     # 동시성 관련 테스트
    │   └── stub/          # 공통 Stub 구현체
    └── config/            # 테스트 설정
```

### DisplayName 작성 규칙
- `[성공/실패] 설명` 형태로 작성
- 테스트의 의도와 예상 결과를 명확히 표현

```java
@DisplayName("[성공] 기본 등급 정책은 2,000,000원 이하의 금액을 허용한다")
@DisplayName("[실패] 기본 등급 정책은 2,000,001원 이상의 금액을 허용하지 않는다")
@DisplayName("[성공] 동시성 - 10개의 예약 이체 요청이 동시에 와도 전부 성공한다")
```

## 테스트 유형별 작성 방법

### 1. 단위 테스트 (Unit Test)
단일 클래스나 메서드의 동작을 검증합니다.

```java
@Test
@DisplayName("[성공] 현재 금액과 주어진 금액을 더하면 새로운 Money 객체에 저장된다")
void plus() {
    // given:
    final Money current = Money.wons(10_000);
    final Money money = Money.wons(5_000);

    // when:
    final Money result = current.plus(money);

    // then:
    assertThat(result).isEqualTo(Money.wons(15_000));
}
```

### 2. 통합 테스트 (Integration Test)
여러 컴포넌트가 함께 동작하는 것을 검증합니다.

```java
@Test
@DisplayName("[성공] 예약 이체를 요청한다.")
void scheduleTransaction() {
    // given: 필요한 의존성들을 모두 준비
    final UserRepository stubUserRepository = new StubUserRepository();
    final AccountRepository stubAccountRepository = new StubAccountRepository();
    // ... 다른 의존성들
    
    final TransactionCommandService sut = new TransactionCommandService(
        // 모든 의존성 주입
    );

    // when: 실제 비즈니스 로직 실행
    sut.scheduleTransaction(request);

    // then: 여러 상태 변화 검증
    assertEquals(expectedBalance, actualBalance);
    assertEquals(expectedTransaction, actualTransaction);
}
```

### 3. 파라미터화 테스트
동일한 로직을 여러 입력값으로 테스트할 때 사용합니다.

```java
@ParameterizedTest(name = "[실패] 현재 금액이 주어진 금액과 같거나 작으면 false를 반환한다 input = {0}")
@ValueSource(doubles = {10_000, 9_999, 0})
void isGreaterThanFail(final double amount) {
    // given:
    final Money current = Money.wons(amount);
    final Money money = Money.wons(10_000);

    // when:
    final boolean greaterThan = current.isGreaterThan(money);

    // then:
    assertThat(greaterThan).isFalse();
}
```

## 테스트 데이터 준비

### 1. Fixture 패턴 사용
재사용 가능한 테스트 데이터 생성을 위해 Fixture 클래스를 활용합니다.

```java
public class AccountFixture {
    private Long id = null;
    private String accountNumber = null;
    private BankCode bankCode = null;
    private Long userId = null;
    private AccountType type = AccountType.SAVINGS;
    private Money amount = Money.ZERO;

    public static AccountFixture anAccount() {
        return new AccountFixture();
    }

    public AccountFixture withId(Long id) {
        this.id = id;
        return this;
    }

    public AccountFixture withBankCode(BankCode bankCode) {
        this.bankCode = bankCode;
        return this;
    }

    // ... 다른 빌더 메서드들

    public Account build() {
        return new Account(id, bankCode, accountNumber, userId, type, amount);
    }
}
```

### 2. Fixture 사용 예시
```java
final Account account = AccountFixture.anAccount()
        .withUserId(1L)
        .withBankCode(BankCode.TOSS_BANK)
        .withAccountNumber("1234567890")
        .withBalance(Money.wons(100_000))
        .build();
```

## 의존성 처리

### 1. Stub 구현체 사용
외부 의존성은 Stub 구현체로 대체합니다.

```java
@Repository
public class StubAccountRepository implements AccountRepository {
    private final Map<Long, Account> store = new HashMap<>();
    private final AtomicLong id = new AtomicLong(1L);

    @Override
    public Account save(final Account account) {
        final long accountId = this.id.getAndIncrement();
        final Account newAccount = new Account(/* 생성자 파라미터 */);
        store.put(accountId, newAccount);
        return newAccount;
    }

    @Override
    public Optional<Account> findByBankAndAccountNumber(final BankCode bank, final String accountNumber) {
        return store.values()
                .stream()
                .filter(account -> account.getBankCode().equals(bank) && account.getAccountNumber().equals(accountNumber))
                .findFirst();
    }
}
```

### 2. 테스트 설정 분리
테스트용 빈 설정을 별도로 관리합니다.

```java
@TestConfiguration
public class TestConfig {
    @Bean
    public UserRepository userRepository() {
        return new StubUserRepository();
    }

    @Bean
    public ScheduleTransactionRepository scheduleTransactionRepository() {
        return new StubScheduleTransactionRepository();
    }
}
```

## 동시성 테스트

### 1. 기본 동시성 테스트 구조
```java
@Test
@DisplayName("[성공] 동시성 - 10개의 예약 이체 요청이 동시에 와도 전부 성공한다.")
void allTenConcurrentRequestsShouldSucceed() throws InterruptedException {
    // given:
    TransactionCommandService service = createStubService();
    ExecutorService executor = Executors.newFixedThreadPool(10);
    CountDownLatch latch = new CountDownLatch(10);
    AtomicInteger successCount = new AtomicInteger();

    // when:
    for (int i = 0; i < 10; i++) {
        executor.submit(() -> {
            try {
                service.scheduleTransaction(request);
                successCount.incrementAndGet();
            } finally {
                latch.countDown();
            }
        });
    }
    latch.await();
    executor.shutdown();

    // then:
    assertEquals(10, successCount.get());
}
```

### 2. 락 테스트
```java
@Test
@DisplayName("[성공] 동시성 - 락 획득 성공")
void shouldAcquireOnlyOneThread() throws InterruptedException, ExecutionException {
    // given:
    final LockManager lockManager = new InMemoryLockManager();
    final ExecutorService EXECUTOR = Executors.newFixedThreadPool(10);
    final CountDownLatch ready = new CountDownLatch(THREAD_COUNT);
    final CountDownLatch start = new CountDownLatch(1);

    // when:
    final List<Future<Boolean>> results = new ArrayList<>();
    for (int i = 0; i < THREAD_COUNT; i++) {
        Future<Boolean> future = EXECUTOR.submit(() -> {
            ready.countDown();
            start.await();
            return lockManager.acquire(KEY, 1L, TimeUnit.SECONDS);
        });
        results.add(future);
    }
    ready.await();
    start.countDown();

    // then:
    int successCount = 0;
    for (Future<Boolean> result : results) {
        if (result.get()) {
            successCount++;
        }
    }
    assertThat(successCount).isEqualTo(1);
}
```

## 어노테이션 사용법

### 1. 기본 테스트 어노테이션
- `@Test`: 일반적인 테스트 메서드
- `@DisplayName`: 테스트 설명 (한글 가능)
- `@ParameterizedTest`: 파라미터화 테스트

### 2. 파라미터 제공 어노테이션
- `@ValueSource`: 단일 값 배열 제공
- `@CsvSource`: CSV 형태 데이터 제공
- `@MethodSource`: 메서드로 데이터 제공

### 3. 스프링 테스트 어노테이션
- `@TestConfiguration`: 테스트용 설정 클래스
- `@Repository`: 테스트용 저장소 구현체

## 어설션 라이브러리 사용

### 1. JUnit 기본 어설션
```java
assertEquals(expected, actual);
assertTrue(condition);
assertFalse(condition);
assertThrows(ExceptionClass.class, () -> {
    // 예외를 발생시킬 코드
});
```

### 2. AssertJ 사용
```java
assertThat(result).isTrue();
assertThat(result).isFalse();
assertThat(result).isEqualTo(expected);
assertThat(list).hasSize(expectedSize);
```

## 주의사항

1. **테스트 독립성**: 각 테스트는 다른 테스트에 영향을 주지 않아야 합니다.
2. **명확한 실패 메시지**: 테스트 실패 시 원인을 쉽게 파악할 수 있도록 작성합니다.
3. **테스트 데이터 격리**: 테스트마다 필요한 데이터를 독립적으로 준비합니다.
4. **동시성 테스트 안정성**: CountDownLatch와 ExecutorService를 활용하여 안정적인 동시성 테스트를 작성합니다.
5. **Stub 구현체**: 실제 구현체와 동일한 인터페이스를 구현하되, 테스트에 필요한 최소한의 로직만 포함합니다. 