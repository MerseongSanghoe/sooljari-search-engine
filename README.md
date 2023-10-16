# sooljari-search-engine
> 주류 큐레이션 플랫폼 서비스 ‘술자리’ 프로젝트의 검색 엔진 서버

## 📚 Stacks
* Java 17
* Spring 6.0.11
  * Spring Boot 3.1.3
  * Spring-data-JPA 3.1.3
  * Spring-data-elasticsearch 5.1.3
* elasticsearch 8.7.1
* MySQL

## 🔗 APIs
### 검색 API
* request URI: `/alcohol/search`
* request param
  * `s`: 검색어
  * `page`(default 0): 페이지 (zero-based)
  * `size`(default 10): 한 페이지의 개수
* request uri example
```
// 사과 검색
/alcohol/search?s=사과

// 바나나 검색 (5개씩 나눈 페이지의 3번째)
/alcohol/search?s=바나나&page=3&size=5
```
* response body

example uri: `/alcohol/search?s=사과&size=2`
```JSON
{
  "data": [
    {
      "score": 0.52354836,
      "id": 2,
      "title": "사과",
      "category": "사과",
      "degree": 10.0,
      "tags": [
        "태그1",
        "태그2"
      ]
    },
    {
      "score": 0.39019167,
      "id": 1,
      "title": "사과즙",
      "category": "사과",
      "degree": 10.0,
      "tags": [
        "태그1",
        "태그2",
        "태그3"
      ]
    }
  ],
  "count": 100
}
```
* `count`: 검색 결과의 총 개수
* `data`: 검색 컨텐츠
  * `score`: 검색 스코어
  * `id`: DB id

## 📔 Notes
> 프로젝트 개발 중 작성한 블로그 글 혹은 문서 링크
* [[Spring Boot] Spring Boot + elasticsearch로 검색 API 구현하기](https://j-1001000.tistory.com/1)
* [[Spring Boot + Docker] Spring Boot 프로젝트를 Docker로 배포하자](https://j-1001000.tistory.com/2)
* [[Lombok] @Builder 어노테이션으로 builder 패턴 사용하기](https://j-1001000.tistory.com/3)
* [[Java] Varargs, “String…” 타입이란?](https://j-1001000.tistory.com/4)