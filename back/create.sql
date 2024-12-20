create table ARTICLES
(
    author_id  bigint,
    created_at datetime(6),
    id         bigint not null auto_increment,
    theme_id   bigint,
    updated_at datetime(6),
    title      varchar(50),
    content    longtext,
    primary key (id)
) engine = InnoDB;
create table COMMENTS
(
    article_id bigint,
    author_id  bigint,
    created_at datetime(6),
    id         bigint not null auto_increment,
    updated_at datetime(6),
    content    varchar(255),
    primary key (id)
) engine = InnoDB;
create table SUBSCRIPTIONS
(
    created_at datetime(6),
    id         bigint not null auto_increment,
    theme_id   bigint,
    updated_at datetime(6),
    user_id    bigint,
    primary key (id)
) engine = InnoDB;
create table THEMES
(
    created_at  datetime(6),
    id          bigint not null auto_increment,
    updated_at  datetime(6),
    name        varchar(200),
    description varchar(500),
    primary key (id)
) engine = InnoDB;
create table USERS
(
    created_at datetime(6),
    id         bigint not null auto_increment,
    updated_at datetime(6),
    username   varchar(20),
    email      varchar(50),
    password   varchar(120),
    primary key (id)
) engine = InnoDB;
alter table USERS
    add constraint UKavh1b2ec82audum2lyjx2p1ws unique (email);
alter table ARTICLES
    add constraint FKjnt23nr1d7wt0sjl2dof5mip5 foreign key (author_id) references USERS (id);
alter table ARTICLES
    add constraint FKb26fanb9ju1i0ym1gswhj0cg6 foreign key (theme_id) references THEMES (id);
alter table COMMENTS
    add constraint FKf64h7ce877ohr18431t5oqcit foreign key (article_id) references ARTICLES (id);
alter table COMMENTS
    add constraint FKbr9p7rllqhrjo97rucg45lhfx foreign key (author_id) references USERS (id);
alter table SUBSCRIPTIONS
    add constraint FKpn4yxeolhg898i28a1p5tvti2 foreign key (theme_id) references THEMES (id);
alter table SUBSCRIPTIONS
    add constraint FKgb4j0qpwv6hdgy7aotoobd4ty foreign key (user_id) references USERS (id);
