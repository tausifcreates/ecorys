create table if not exists worker (
    id            bigint       not null auto_increment primary key,
    nid           varchar(17)  not null unique,
    name          varchar(100) not null,
    mobile_number varchar(11)  not null
);

create table if not exists application (
    id               bigint       not null auto_increment primary key,
    worker_id        bigint       not null,
    application_date date         not null,
    reason           varchar(500) not null,
    status           enum ('SUBMITTED', 'APPROVED', 'REJECTED') not null default 'SUBMITTED',
    approval_date    date,
    index idx_application_worker (worker_id),
    constraint fk_application_worker foreign key (worker_id) references worker (id)
);
