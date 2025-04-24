-- ユーザー情報
CREATE TABLE users (
    id UUID PRIMARY KEY,                             -- ユーザーID（UUID）
    email_address VARCHAR(255) NOT NULL UNIQUE,      -- メールアドレス（ユニーク）
    password VARCHAR(255) NOT NULL,                  -- ハッシュ化されたパスワード
    real_name VARCHAR(50) NOT NULL,                  -- 本名
    user_name VARCHAR(50) NOT NULL UNIQUE,           -- 表示用ユーザー名（ユニーク）
    thumbnail_url TEXT,                              -- サムネイル画像のURL
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 登録日時
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP  -- 更新日時
);

-- ユーザーのログイン履歴
CREATE TABLE user_login_histories (
    id UUID PRIMARY KEY,                             -- 履歴ID（UUID）
    user_id UUID REFERENCES users(id) ON DELETE CASCADE, -- 関連するユーザーID
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- ログイン日時
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP  -- レコード更新日時
);

-- コース情報
CREATE TABLE courses (
    id UUID PRIMARY KEY,                             -- コースID
    thumbnail_url TEXT,                              -- サムネイル画像のURL
    title VARCHAR(255) NOT NULL,                     -- コースタイトル
    description TEXT,                                -- コース説明
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 作成日時
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP  -- 更新日時
);

-- レッスングループ（複数レッスンをまとめる）
CREATE TABLE lesson_groups (
    id UUID PRIMARY KEY,                             -- グループID
    course_id UUID NOT NULL REFERENCES courses(id) ON DELETE CASCADE, -- 所属コース
    name VARCHAR(255) NOT NULL,                      -- グループ名
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 作成日時
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP  -- 更新日時
);

-- レッスン情報
CREATE TABLE lessons (
    id UUID PRIMARY KEY,                             -- レッスンID
    lesson_group_id UUID NOT NULL REFERENCES lesson_groups(id) ON DELETE CASCADE, -- 所属グループ
    course_id UUID NOT NULL REFERENCES courses(id) ON DELETE CASCADE, -- 所属コース
    title VARCHAR(255) NOT NULL,                     -- レッスンタイトル
    description TEXT,                                -- レッスンの説明
    video_url TEXT,                                  -- 動画のURL
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 作成日時
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP  -- 更新日時
);

-- お知らせ
CREATE TABLE news (
    id UUID PRIMARY KEY,                             -- お知らせID
    title VARCHAR(255) NOT NULL,                     -- タイトル
    content TEXT,                                    -- 本文
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 作成日時
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP  -- 更新日時
);

-- 中間テーブル：ユーザーが受講したレッスン
CREATE TABLE user_lessons (
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,   -- ユーザーID
    lesson_id UUID REFERENCES lessons(id) ON DELETE CASCADE, -- レッスンID
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 登録日時
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 更新日時
    PRIMARY KEY (user_id, lesson_id)                    -- 複合主キー
);

-- パスワード再設定トークン管理
CREATE TABLE password_reset_tokens (
    id UUID PRIMARY KEY,                             -- トークンID（UUID）
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE, -- 対象ユーザーID
    token VARCHAR(255) NOT NULL UNIQUE,              -- トークン文字列（URLに付加）
    expires_at TIMESTAMP NOT NULL,                 -- 有効期限（例：30分）
    used_at TIMESTAMP                              -- 使用済み日時（nullなら未使用）
);

-- ログイン状態を保持するテーブル（Spring Security 用）
CREATE TABLE persistent_logins (
    series VARCHAR(64) PRIMARY KEY,                  -- 一意なトークン識別子（UUIDなど）
	username VARCHAR(255) NOT NULL REFERENCES users(email_address) ON UPDATE CASCADE, -- ユーザー名（通常はemail）
    token VARCHAR(64) NOT NULL,                      -- 実際のログイントークン（UUIDなど）
    last_used TIMESTAMP NOT NULL                     -- 最後に使用された日時
);
