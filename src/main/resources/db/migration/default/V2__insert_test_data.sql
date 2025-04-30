-- ユーザー
INSERT INTO public.users
(id, email_address, "password", real_name, user_name, thumbnail_url, user_role, created_at, updated_at) 
VALUES
(gen_random_uuid(), 'kanri@test.com', '$2a$08$1ActTrb5pvrY.LX2onroGu7ewYWMTHffGXs1h3oLh4Ho.l/LgrbZe', '管理者太郎', 'kanri123', '/uploads/kanrisya.png', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO public.users 
(id, email_address, "password", real_name, user_name, thumbnail_url, user_role, created_at, updated_at) 
VALUES
(gen_random_uuid(), 'ippan@test.com', '$2a$08$1ActTrb5pvrY.LX2onroGu7ewYWMTHffGXs1h3oLh4Ho.l/LgrbZe', '一般次郎', 'ippan123', '/uploads/ippan.png', 'GENERAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- お知らせ
INSERT INTO public.news (id, title, "content", created_at, updated_at) VALUES
(gen_random_uuid(), 'なんでもお悩み相談会開催のご案内', '7月15日にGoogleMeetにて「なんでもお悩み相談会」を開催します。学習やキャリアの悩みなど、気軽にご参加ください。', '2025-04-20 10:00:00', '2025-04-20 10:00:00');
INSERT INTO public.news (id, title, "content", created_at, updated_at) VALUES
(gen_random_uuid(), 'ポートフォリオ相談会のご案内', '就職活動に向けたポートフォリオの個別相談会を7月第2週に開催します。実際の作品に対して具体的なフィードバックを行います。', '2025-06-25 14:00:00', '2025-06-25 14:00:00');
INSERT INTO public.news (id, title, "content", created_at, updated_at) VALUES
(gen_random_uuid(), '実務案件のご紹介', '現在、受講生には実務案件に参画していただくことが可能です。学んだスキルを実践の場で試し、成長できるチャンスです。希望者は私までご相談ください。', '2025-09-10 12:00:00', '2025-09-10 12:00:00');
INSERT INTO public.news (id, title, "content", created_at, updated_at) VALUES
(gen_random_uuid(), '転職相談会のお知らせ', '11月1日〜3日に私が直接、転職相談会を実施します。履歴書・職務経歴書の添削や企業選びのアドバイスを行いますので、気軽にご参加ください。', '2025-10-25 13:00:00', '2025-10-25 13:00:00');
INSERT INTO public.news (id, title, "content", created_at, updated_at) VALUES
(gen_random_uuid(), '年末年始の授業スケジュールについて', '12月29日〜1月3日は年末年始休講となります。年明けの授業再開は1月4日からを予定しています。振替授業については別途ご案内します。', '2025-12-01 11:45:00', '2025-12-01 11:45:00');

-- コース
INSERT INTO public.courses (id, thumbnail_url, title, description, created_at, updated_at) VALUES('0a0313c2-bb1e-8c58-d458-a8a884bf899a', '/uploads/course_thumbnail_sample.png', '1章:Java', 'Javaの基本文法、条件分岐・繰り返し処理、オブジェクト指向、例外処理までを体系的に学び、開発の基礎を固めます。', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO public.courses (id, thumbnail_url, title, description, created_at, updated_at) VALUES('e83d6d3b-02e1-040e-b912-f401746a01e2', '/uploads/course_thumbnail_sample.png', '2章:SQL', 'リレーショナルデータベースの基礎から、SELECT文・JOIN・サブクエリ・集計関数など実務で必要なSQLスキルを習得します。', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO public.courses (id, thumbnail_url, title, description, created_at, updated_at) VALUES('f9f3c9d2-062c-b97c-f0b7-f141e81bdf1a', '/uploads/course_thumbnail_sample.png', '3章:HTML&CSS', 'Webページの構造を作るHTMLと、見た目を整えるCSSの基本からレスポンシブ対応まで、Web制作の土台を学びます。', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO public.courses (id, thumbnail_url, title, description, created_at, updated_at) VALUES('c8c7429a-08b0-578b-6482-23090d5b4aef', '/uploads/course_thumbnail_sample.png', '4章:JavaScript', '変数・関数・制御構文などの基本から、DOM操作やイベント処理、非同期通信まで動的Web開発の基礎を習得します。', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO public.courses (id, thumbnail_url, title, description, created_at, updated_at) VALUES('0f197212-45b6-3e56-54ce-01f600e38ca1', '/uploads/course_thumbnail_sample.png', '5章:Git', 'Gitによるバージョン管理の基本操作から、ブランチ運用、マージ、GitHubでのチーム開発までを実践的に学びます。', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO public.courses (id, thumbnail_url, title, description, created_at, updated_at) VALUES('add6e203-9557-aebb-59a9-52cda9b9ef5b', '/uploads/course_thumbnail_sample.png', '6章:Spring', 'Spring Bootを使ったアプリ開発を通じて、MVCモデル、DI、REST APIなどのJavaバックエンド技術を体系的に学習します。', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO public.courses (id, thumbnail_url, title, description, created_at, updated_at) VALUES('f6a51c4f-91bc-1df5-3880-723754ae7089', '/uploads/course_thumbnail_sample.png', '7章:Docker', 'Dockerの仕組みや基本コマンドから、開発環境のコンテナ化、本番環境へのデプロイ方法まで幅広く学びます。', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO public.courses (id, thumbnail_url, title, description, created_at, updated_at) VALUES('d51a780e-09a6-56d3-15e5-43df65aba790', '/uploads/course_thumbnail_sample.png', '8章:AWS', 'AWSの基本概念を理解し、EC2やS3、RDSなど主要サービスを使って、クラウド上にアプリを構築・運用する方法を学びます。', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO public.courses (id, thumbnail_url, title, description, created_at, updated_at) VALUES('1db4661a-cd40-3724-edf7-0a059d4bcebe', '/uploads/course_thumbnail_sample.png', '9章:CI/CD', 'GitHub Actionsを用いて、ソースコードの自動テスト、ビルド、デプロイまでを自動化するCI/CD環境の構築を学びます。', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- レッスングループ
INSERT INTO lesson_groups (id, course_id, name, created_at, updated_at) VALUES ('d081057a-d7ab-4d07-b9b9-9bf396d53c66', '0a0313c2-bb1e-8c58-d458-a8a884bf899a', 'Java導入', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO lesson_groups (id, course_id, name, created_at, updated_at) VALUES ('29fb64ec-2f99-4a52-89f3-8d1f19f4a2fe', '0a0313c2-bb1e-8c58-d458-a8a884bf899a', 'Java基礎', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO lesson_groups (id, course_id, name, created_at, updated_at) VALUES ('73208444-4419-42c8-bd0b-22ee7096e210', '0a0313c2-bb1e-8c58-d458-a8a884bf899a', 'Java応用', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
