## 대규모 게시판 서비스 구현
service : 하위 모듈로 각 마이크로서비스를 가진다. <br/>
ㄴ article : 게시글 서비스 <br/>
ㄴ comment : 댓글 서비스 <br/>
ㄴ like : 좋아요 서비스 <br/>
ㄴ view : 조회수 서비스 <br/>
ㄴ hot-article : 인기글 서비스 <br/>
ㄴ article-read : 게시글 조회 서비스 <br/>
ㄴ common : 하위 모듈로 개발 편의를 위한 공통 코드를 관리한다. <br/>

## 실행 순서 ( 작성 중 )
1. docker 실행
2. docker-compose up -d
3. [데이터 initialize 먼저 실행](service/article/src/test/kotlin/com/board/article/data/DataInitialize.kt)
