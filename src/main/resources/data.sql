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
