create table all_features (
    features jsonb not null
);
insert into all_features (features) values ('[]'::jsonb);

create table client_features (
  id varchar(255) PRIMARY KEY,
  features jsonb not null
);
