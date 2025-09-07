-- User
CREATE TABLE IF NOT EXISTS users
(
	id
	uuid
	PRIMARY
	KEY
	NOT
	NULL
	DEFAULT
	uuid_generate_v4
(
), name text, username text, email text, status text NOT NULL, role text NOT NULL, auth_id text NOT NULL, auth_provider text NOT NULL, avatar_url text, created_at timestamp
	with time zone NOT NULL DEFAULT NOW(), logged_in_at timestamp with time zone, updated_at timestamp with time zone, deleted_at timestamp
	with time zone );

CREATE UNIQUE INDEX IF NOT EXISTS index_users_on_auth_id_and_auth_provider ON users(auth_id, auth_provider) WHERE deleted_at IS NULL;

CREATE TRIGGER trigger_users_updated_at
	BEFORE UPDATE
	ON users
	FOR EACH ROW EXECUTE FUNCTION update_updated_at();

-- Refresh Tokens
CREATE TABLE IF NOT EXISTS refresh_tokens
(
	id
	uuid
	PRIMARY
	KEY
	NOT
	NULL
	DEFAULT
	uuid_generate_v4
(
), user_id uuid NOT NULL, token text NOT NULL, role text NOT NULL, created_at timestamp
	with time zone NOT NULL DEFAULT NOW(), updated_at timestamp with time zone, deleted_at timestamp
	with time zone );

CREATE INDEX IF NOT EXISTS index_refresh_tokens_on_user_id ON refresh_tokens(user_id) WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS index_refresh_tokens_on_token ON refresh_tokens(token) WHERE deleted_at IS NULL;

CREATE TRIGGER trigger_refresh_tokens_updated_at
	BEFORE UPDATE
	ON refresh_tokens
	FOR EACH ROW EXECUTE FUNCTION update_updated_at();

-- Auth Provider Tokens
CREATE TABLE IF NOT EXISTS auth_provider_tokens
(
	id
	uuid
	PRIMARY
	KEY
	NOT
	NULL
	DEFAULT
	uuid_generate_v4
(
), user_id uuid NOT NULL, auth_id text NOT NULL, token text NOT NULL, created_at timestamp
	with time zone NOT NULL DEFAULT NOW(), updated_at timestamp with time zone, deleted_at timestamp
	with time zone );

CREATE INDEX IF NOT EXISTS index_auth_provider_tokens_on_user_id ON auth_provider_tokens(user_id) WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS index_auth_provider_tokens_on_token ON auth_provider_tokens(token) WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS index_auth_provider_tokens_on_auth_id ON auth_provider_tokens(auth_id) WHERE deleted_at IS NULL;

CREATE TRIGGER trigger_auth_provider_tokens_updated_at
	BEFORE UPDATE
	ON auth_provider_tokens
	FOR EACH ROW EXECUTE FUNCTION update_updated_at();
