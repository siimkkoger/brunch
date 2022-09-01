INSERT INTO public.account (enabled, password, email, username)
VALUES (true, '$2a$12$7ixvvDBcgvL91pCyft1b3OY6h3U.yi2Z5EYGkUKhLYSS17lcEJGp2', 'user1@brunch.com', 'user1');
INSERT INTO public.account (enabled, password, email, username)
VALUES (true, '$2a$12$7ixvvDBcgvL91pCyft1b3OY6h3U.yi2Z5EYGkUKhLYSS17lcEJGp2', 'user2@brunch.com', 'user2');
INSERT INTO public.account (enabled, password, email, username)
VALUES (true, '$2a$12$7ixvvDBcgvL91pCyft1b3OY6h3U.yi2Z5EYGkUKhLYSS17lcEJGp2', 'user3@brunch.com', 'user3');

INSERT INTO public.account (enabled, password, email, username)
VALUES (true, '$2a$12$7ixvvDBcgvL91pCyft1b3OY6h3U.yi2Z5EYGkUKhLYSS17lcEJGp2', 'admin1@brunch.com', 'admin1');
INSERT INTO public.account (enabled, password, email, username)
VALUES (true, '$2a$12$7ixvvDBcgvL91pCyft1b3OY6h3U.yi2Z5EYGkUKhLYSS17lcEJGp2', 'admin2@brunch.com', 'admin2');
INSERT INTO public.account (enabled, password, email, username)
VALUES (true, '$2a$12$7ixvvDBcgvL91pCyft1b3OY6h3U.yi2Z5EYGkUKhLYSS17lcEJGp2', 'admin3@brunch.com', 'admin3');

-- ACCOUNTS_ROLES
INSERT INTO public.accounts_roles (account_id, role_id)
VALUES ((select id from public.account where email='admin1@brunch.com'), (select id from public.roles where code='ADMIN'));
INSERT INTO public.accounts_roles (account_id, role_id)
VALUES ((select id from public.account where email='admin2@brunch.com'), (select id from public.roles where code='ADMIN'));
INSERT INTO public.accounts_roles (account_id, role_id)
VALUES ((select id from public.account where email='admin3@brunch.com'), (select id from public.roles where code='ADMIN'));
INSERT INTO public.accounts_roles (account_id, role_id)
VALUES ((select id from public.account where email='user1@brunch.com'), (select id from public.roles where code='USER'));
INSERT INTO public.accounts_roles (account_id, role_id)
VALUES ((select id from public.account where email='user2@brunch.com'), (select id from public.roles where code='USER'));
INSERT INTO public.accounts_roles (account_id, role_id)
VALUES ((select id from public.account where email='user3@brunch.com'), (select id from public.roles where code='USER'));