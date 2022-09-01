CREATE TABLE public.account
(
  id             serial         NOT NULL PRIMARY KEY,
  enabled        boolean        NOT NULL default true,
  username       varchar(255)   UNIQUE NOT NULL,
  password       varchar(255)   NOT NULL,
  email          varchar(255)   UNIQUE NOT NULL,

  created_at     timestamp      NOT NULL DEFAULT current_timestamp,
  updated_at     timestamp      NOT NULL DEFAULT current_timestamp,
  deleted_at     timestamp      NULL
);

CREATE TABLE public.roles
(
    id            serial         NOT NULL PRIMARY KEY,
    code          varchar(255)   UNIQUE NOT NULL,

    created_at     timestamp      NOT NULL DEFAULT current_timestamp,
    updated_at     timestamp      NOT NULL DEFAULT current_timestamp,
    deleted_at     timestamp      NULL
);

CREATE TABLE public.accounts_roles
(
    account_id     serial         NOT NULL,
    role_id        serial         NOT NULL,

    created_at     timestamp      NOT NULL DEFAULT current_timestamp,
    updated_at     timestamp      NOT NULL DEFAULT current_timestamp,
    deleted_at     timestamp      NULL,
    PRIMARY KEY (account_id, role_id)
);

-- ROLES
INSERT INTO public.roles (code) VALUES ('ADMIN');
INSERT INTO public.roles (code) VALUES ('USER');