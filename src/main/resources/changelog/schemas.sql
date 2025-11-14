--liquibase formatted sql
--changeset Author:create_schemas context:production,test

CREATE SCHEMA IF NOT EXISTS core;
