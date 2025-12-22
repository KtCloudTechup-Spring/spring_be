-- 커뮤니티 데이터 (중복 방지)

INSERT INTO community (name)
SELECT '풀스택'
WHERE NOT EXISTS (
    SELECT 1 FROM community WHERE name = '풀스택'
);

INSERT INTO community (name)
SELECT '프론트엔드'
WHERE NOT EXISTS (
    SELECT 1 FROM community WHERE name = '프론트엔드'
);

INSERT INTO community (name)
SELECT '백엔드'
WHERE NOT EXISTS (
    SELECT 1 FROM community WHERE name = '백엔드'
);

INSERT INTO community (name)
SELECT '생성형 AI'
WHERE NOT EXISTS (
    SELECT 1 FROM community WHERE name = '생성형 AI'
);

INSERT INTO community (name)
SELECT '사이버 보안'
WHERE NOT EXISTS (
    SELECT 1 FROM community WHERE name = '사이버 보안'
);

INSERT INTO community (name)
SELECT '클라우드 인프라'
WHERE NOT EXISTS (
    SELECT 1 FROM community WHERE name = '클라우드 인프라'
);

INSERT INTO community (name)
SELECT '클라우드 네이티브'
WHERE NOT EXISTS (
    SELECT 1 FROM community WHERE name = '클라우드 네이티브'
);

INSERT INTO community (name)
SELECT '프로덕트 디자인'
WHERE NOT EXISTS (
    SELECT 1 FROM community WHERE name = '프로덕트 디자인'
);

INSERT INTO community (name)
SELECT '프로덕트 매니지먼트'
WHERE NOT EXISTS (
    SELECT 1 FROM community WHERE name = '프로덕트 매니지먼트'
);


-- 커뮤니티별 고정 채팅방 생성

INSERT IGNORE INTO chatting_room
(
  id,
  created_at,
  updated_at,
  closed_at,
  description,
  is_active,
  max_members,
  name,
  community_id,
  created_by
)
VALUES
(NULL, NOW(), NULL, NULL, '풀스택 과정 커뮤니티 채팅방', true, 200, '풀스택 채팅방', 1, NULL),
(NULL, NOW(), NULL, NULL, '프론트엔드 과정 커뮤니티 채팅방', true, 200, '프론트엔드 채팅방', 2, NULL),
(NULL, NOW(), NULL, NULL, '백엔드 과정 커뮤니티 채팅방', true, 200, '백엔드 채팅방', 3, NULL),
(NULL, NOW(), NULL, NULL, '생성형 AI 과정 커뮤니티 채팅방', true, 200, '생성형 AI 채팅방', 4, NULL),
(NULL, NOW(), NULL, NULL, '사이버 보안 과정 커뮤니티 채팅방', true, 200, '사이버 보안 채팅방', 5, NULL),
(NULL, NOW(), NULL, NULL, '클라우드 인프라 과정 커뮤니티 채팅방', true, 200, '클라우드 인프라 채팅방', 6, NULL),
(NULL, NOW(), NULL, NULL, '클라우드 네이티브 과정 커뮤니티 채팅방', true, 200, '클라우드 네이티브 채팅방', 7, NULL),
(NULL, NOW(), NULL, NULL, '프로덕트 디자인 과정 커뮤니티 채팅방', true, 200, '프로덕트 디자인 채팅방', 8, NULL),
(NULL, NOW(), NULL, NULL, '프로덕트 매니지먼트 과정 커뮤니티 채팅방', true, 200, '프로덕트 매니지먼트 채팅방', 9, NULL);

