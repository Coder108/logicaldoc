create table ld_bookmark (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_userid bigint not null, ld_docid bigint not null, ld_title varchar(255) not null, ld_description varchar(4000), ld_position int not null, ld_filetype varchar(40), primary key (ld_id));
create table ld_document (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_immutable int not null, ld_customid varchar(255), ld_title varchar(255), ld_version varchar(10), ld_fileversion varchar(10), ld_date timestamp, ld_creation timestamp not null, ld_publisher varchar(255), ld_publisherid bigint not null, ld_creator varchar(255), ld_creatorid bigint not null, ld_status int, ld_type varchar(255), ld_lockuserid bigint, ld_source varchar(4000), ld_sourceauthor varchar(255), ld_sourcedate timestamp null, ld_sourceid varchar(1000), ld_sourcetype varchar(255), ld_object varchar(1000), ld_coverage varchar(255), ld_language varchar(10), ld_filename varchar(255), ld_filesize bigint, ld_indexed int not null, ld_barcoded int not null, ld_signed int not null, ld_digest varchar(255), ld_recipient varchar(1000), ld_folderid bigint, ld_templateid bigint, ld_exportstatus int not null, ld_exportid bigint, ld_exportname varchar(255), ld_exportversion varchar(10), ld_docref bigint, ld_deleteuserid bigint, ld_rating int, primary key (ld_id));
create table ld_document_ext (ld_docid bigint not null, ld_mandatory int not null, ld_type int not null, ld_position int not null, ld_stringvalue varchar(4000), ld_intvalue bigint, ld_doublevalue float, ld_datevalue timestamp null, ld_name varchar(255) not null, ld_label varchar(255), primary key (ld_docid, ld_name));
create table ld_dcomment (ld_threadid bigint not null, ld_replyto int, ld_replypath varchar(255), ld_userid bigint not null, ld_username varchar(255), ld_date timestamp, ld_subject varchar(255), ld_body varchar(4000), ld_deleted int not null, ld_id int not null, primary key (ld_threadid, ld_id));
create table ld_dthread (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_docid bigint not null, ld_creation timestamp, ld_creatorid bigint not null, ld_creatorname varchar(255), ld_lastpost timestamp, ld_subject varchar(255), ld_replies int, ld_views int, primary key (ld_id));
create table ld_generic (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_type varchar(255) not null, ld_subtype varchar(255) not null, ld_string1 varchar(4000), ld_string2 varchar(4000), ld_integer1 int, ld_integer2 int, ld_double1 float, ld_double2 float, ld_date1 timestamp, ld_date2 timestamp, primary key (ld_id));
create table ld_generic_ext (ld_genid bigint not null, ld_mandatory int not null, ld_type int not null, ld_position int not null, ld_stringvalue varchar(4000), ld_intvalue bigint, ld_doublevalue float, ld_datevalue timestamp null, ld_name varchar(255) not null, ld_label varchar(255), primary key (ld_genid, ld_name));
create table ld_group (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_name varchar(255) not null, ld_description varchar(255), ld_type int not null, primary key (ld_id));
create table ld_group_ext (ld_groupid bigint not null, ld_mandatory int not null, ld_type int not null, ld_position int not null, ld_stringvalue varchar(4000), ld_intvalue bigint, ld_doublevalue float, ld_datevalue timestamp null, ld_name varchar(255) not null, ld_label varchar(255), primary key (ld_groupid, ld_name));
create table ld_history (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_docid bigint, ld_folderid bigint not null, ld_userid bigint, ld_date timestamp, ld_username varchar(255), ld_event varchar(255), ld_comment varchar(4000), ld_version varchar(10), ld_title varchar(255), ld_path varchar(4000), ld_notified int not null, ld_sessionid varchar(255), ld_new int, ld_filename varchar(255), primary key (ld_id));
create table ld_tag (ld_docid bigint not null, ld_tag varchar(255));
create table ld_link (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_type varchar(255) not null, ld_docid1 bigint, ld_docid2 bigint, primary key (ld_id));
create table ld_menu (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_text varchar(255), ld_parentid bigint not null, ld_securityref bigint, ld_icon varchar(255), ld_type int not null, ld_description varchar(4000), primary key (ld_id));
create table ld_menugroup (ld_menuid bigint not null, ld_groupid bigint not null, ld_write int not null, primary key (ld_menuid, ld_groupid, ld_write));
create table ld_recipient (ld_messageid bigint not null, ld_name varchar(255) not null, ld_address varchar(255) not null, ld_mode varchar(255) not null, ld_type int not null);
create table ld_systemmessage (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_author varchar(255), ld_messagetext varchar(2000), ld_subject varchar(255), ld_sentdate timestamp not null, ld_datescope int, ld_prio int, ld_confirmation int, ld_red int not null, ld_lastnotified timestamp, ld_status int not null, ld_trials int, ld_type int not null, primary key (ld_id));
create table ld_template (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_name varchar(255) not null, ld_description varchar(2000), ld_readonly int not null, ld_type int not null, ld_category int not null, primary key (ld_id));
create table ld_template_ext (ld_templateid bigint not null, ld_mandatory int not null, ld_type int not null, ld_position int not null, ld_stringvalue varchar(4000), ld_intvalue bigint, ld_doublevalue float, ld_datevalue timestamp null, ld_name varchar(255) not null, ld_label varchar(255), primary key (ld_templateid, ld_name));
create table ld_ticket (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_ticketid varchar(255) not null, ld_docid bigint not null, ld_userid bigint not null, ld_type int not null, ld_creation timestamp not null, ld_expired timestamp not null, ld_count int not null, primary key (ld_id));
create table ld_user (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_enabled int not null, ld_username varchar(255) not null, ld_password varchar(255), ld_name varchar(255), ld_firstname varchar(255), ld_street varchar(255), ld_postalcode varchar(255), ld_city varchar(255), ld_country varchar(255), ld_state varchar(255), ld_language varchar(10), ld_email varchar(255), ld_telephone varchar(255), ld_telephone2 varchar(255), ld_type int not null, ld_passwordchanged timestamp, ld_passwordexpires int not null, ld_source int not null, ld_quota bigint not null, ld_quotacount bigint not null, ld_signatureid varchar(255), ld_signatureinfo varchar(255), primary key (ld_id));
create table ld_user_history (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_userid bigint, ld_date timestamp, ld_username varchar(255), ld_event varchar(255), ld_comment varchar(4000), ld_notified int not null, ld_sessionid varchar(255), ld_new int, ld_filename varchar(255), primary key (ld_id));
create table ld_userdoc (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_docid bigint not null, ld_userid bigint not null, ld_date timestamp, primary key (ld_id));
create table ld_usergroup (ld_groupid bigint not null, ld_userid bigint not null, primary key (ld_groupid, ld_userid));
create table ld_version (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_immutable int not null, ld_customid varchar(255), ld_title varchar(255), ld_version varchar(10), ld_fileversion varchar(10), ld_date timestamp, ld_creation timestamp, ld_publisher varchar(255), ld_publisherid bigint not null,  ld_creator varchar(255), ld_creatorid bigint not null, ld_status int, ld_type varchar(255), ld_lockuserid bigint, ld_source varchar(4000), ld_sourceauthor varchar(255), ld_sourcedate timestamp null, ld_sourceid varchar(1000), ld_sourcetype varchar(255), ld_object varchar(1000), ld_coverage varchar(255), ld_language varchar(10), ld_filename varchar(255), ld_filesize bigint, ld_indexed int not null, ld_barcoded int not null, ld_signed int not null, ld_digest varchar(255), ld_recipient varchar(1000), ld_folderid bigint, ld_foldername varchar(1000), ld_templateid bigint, ld_templatename varchar(1000), ld_tgs varchar(1000), ld_username varchar(255), ld_userid bigint, ld_versiondate timestamp, ld_comment varchar(1000),ld_event varchar(255), ld_documentid bigint not null, ld_exportstatus int not null, ld_exportid bigint, ld_exportname varchar(255), ld_exportversion varchar(10), ld_deleteuserid bigint, primary key (ld_id));
create table ld_version_ext (ld_versionid bigint not null, ld_mandatory int not null, ld_type int not null, ld_position int not null, ld_stringvalue varchar(4000), ld_intvalue bigint, ld_doublevalue float, ld_datevalue timestamp null, ld_name varchar(255) not null, ld_label varchar(255), primary key (ld_versionid, ld_name));
create table ld_folder(ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_name varchar(255), ld_parentid bigint not null, ld_securityref bigint, ld_description varchar(4000), ld_type int not null, ld_creation timestamp, ld_creator varchar(255), ld_creatorid bigint, primary key (ld_id));
create table ld_foldergroup (ld_folderid bigint not null, ld_groupid bigint not null, ld_write int not null, ld_add int not null, ld_security int not null, ld_immutable int not null, ld_delete int not null, ld_rename int not null, ld_import int not null, ld_export int not null, ld_sign int not null, ld_archive int not null, ld_workflow int not null, ld_download int not null, primary key (ld_folderid, ld_groupid, ld_write, ld_add, ld_security, ld_immutable, ld_delete, ld_rename, ld_import, ld_export, ld_sign, ld_archive, ld_workflow, ld_download));
create table ld_feedmessage (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_guid varchar(512) null, ld_title varchar(512) null, ld_description  varchar(4000) null, ld_link varchar(512) null, ld_pubdate timestamp, ld_read int not null, primary key (ld_id));
create table ld_rating (ld_id bigint not null, ld_lastmodified timestamp not null, ld_deleted int not null, ld_docid bigint not null, ld_userid bigint not null, ld_vote int not null, primary key (ld_id));

alter table ld_document add constraint FK75ED9C0276C86307 foreign key (ld_templateid) references ld_template(ld_id);
alter table ld_document add constraint FK75ED9C027C565C60 foreign key (ld_folderid) references ld_folder(ld_id);
alter table ld_document_ext add constraint FK4E0884647C693DFD foreign key (ld_docid) references ld_document(ld_id);
alter table ld_generic_ext add constraint FK913AF772CF8376C7 foreign key (ld_genid) references ld_generic(ld_id);
alter table ld_group_ext add constraint FKB728EA5A76F11EA1 foreign key (ld_groupid) references ld_group(ld_id);
alter table ld_tag add constraint FK55BBDA227C693DFD foreign key (ld_docid) references ld_document(ld_id);
alter table ld_link add constraint FK1330661CADD6217 foreign key (ld_docid2) references ld_document(ld_id);
alter table ld_link add constraint FK1330661CADD6216 foreign key (ld_docid1) references ld_document(ld_id);
alter table ld_menugroup add constraint FKB4F7F679AA456AD1 foreign key (ld_menuid) references ld_menu(ld_id);
alter table ld_usergroup add constraint FK2435438DB8B12CA9 foreign key (ld_userid) references ld_user(ld_id);
alter table ld_usergroup add constraint FK2435438D76F11EA1 foreign key (ld_groupid) references ld_group(ld_id);
alter table ld_version add constraint FK9B3BD9118A053CE foreign key (ld_documentid) references ld_document(ld_id);
alter table ld_version_ext add constraint FK78C3A1F3B90495EE foreign key (ld_versionid) references ld_version(ld_id);
alter table ld_dcomment add constraint FKF2C40628DBB5BF4 foreign key (ld_threadid) references ld_dthread(ld_id);
alter table ld_template_ext add constraint FK6BABB84376C86307 foreign key (ld_templateid) references ld_template(ld_id);
alter table ld_recipient add constraint FK406A04126621DEBE foreign key (ld_messageid) references ld_systemmessage(ld_id);

alter table ld_ticket add constraint FK_TICKET_USER foreign key (ld_userid) references ld_user(ld_id) on delete cascade;
alter table ld_menugroup add constraint FK_MENUGROUP_GROUP foreign key (ld_groupid) references ld_group(ld_id) on delete cascade;
alter table ld_userdoc add constraint FK_USERDOC_DOC foreign key (ld_docid) references ld_document(ld_id) on delete cascade;
alter table ld_userdoc add constraint FK_USERDOC_USER foreign key (ld_userid) references ld_user(ld_id) on delete cascade;
alter table ld_menu add constraint FK_MENU_PARENT foreign key (ld_parentid) references ld_menu(ld_id) on delete cascade;
alter table ld_folder add constraint FK_FOLDER_PARENT foreign key (ld_parentid) references ld_folder(ld_id) on delete cascade;
alter table ld_foldergroup add constraint FK_FGROUP_FOLDER foreign key (ld_folderid) references ld_folder(ld_id) on delete cascade;


--On MySQL AK fields cannot be larger than
create unique index  AK_DOCUMENT on ld_document (ld_customid);
create unique index  AK_USER on ld_user (ld_username);
create unique index  AK_GROUP on ld_group (ld_name);  
create unique index  AK_TICKET on ld_ticket (ld_ticketid);
create unique index  AK_USERGROUP on ld_usergroup (ld_groupid, ld_userid);
create unique index  AK_LINK on ld_link (ld_docid1, ld_docid2, ld_type);
create unique index  AK_TEMPLATE on ld_template (ld_name);
create unique index  AK_GENERIC on ld_generic (ld_type, ld_subtype);
create unique index  AK_VERSION on ld_version (ld_documentid, ld_version);
create unique index  AK_RATING on ld_rating (ld_docid, ld_userid);
alter table ld_version add constraint FK_VERSION_USER foreign key (ld_userid) references ld_user(ld_id);


insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_icon,ld_type)
values     (2,CURRENT_TIMESTAMP,0,'administration',2,'menu.png',1);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_icon,ld_type)
values     (9,CURRENT_TIMESTAMP,0,'security',2,'menu.png',1);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_icon,ld_type)
values     (14,CURRENT_TIMESTAMP,0,'tasks',2,'menu.png',1);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_icon,ld_type)
values     (-2,CURRENT_TIMESTAMP,0,'lastchanges',2,'menu.png',1);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_icon,ld_type)
values     (3,CURRENT_TIMESTAMP,0,'clienttools',2,'menu.png',1);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_icon,ld_type)
values     (17,CURRENT_TIMESTAMP,0,'customid',2,'menu.png',1);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_icon,ld_type)
values     (16,CURRENT_TIMESTAMP,0,'tools',2,'menu.png',1);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_icon,ld_type)
values     (8,CURRENT_TIMESTAMP,0,'impex',2,'menu.png',1);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_icon,ld_type)
values     (25,CURRENT_TIMESTAMP,0,'documentmetadata',2,'menu.png',1);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_icon,ld_type)
values     (23,CURRENT_TIMESTAMP,0,'workflow',2,'menu.png',1);

insert into ld_menu
           (ld_id,ld_lastmodified,ld_deleted,ld_text,ld_parentid,ld_icon,ld_type)
values     (7,CURRENT_TIMESTAMP,0,'settings',2,'menu.png',1);

insert into ld_group
values     (1,CURRENT_TIMESTAMP,0,'admin','Group of admins',0);

insert into ld_group
values     (2,CURRENT_TIMESTAMP,0,'author','Group of authors',0);

insert into ld_group
values     (3,CURRENT_TIMESTAMP,0,'guest','Group of guests',0);

insert into ld_group
values     (4,CURRENT_TIMESTAMP,0,'poweruser','Group of power users',0);

insert into ld_user
           (ld_id,ld_lastmodified,ld_deleted,ld_enabled,ld_username,ld_password,ld_name,ld_firstname,ld_street,ld_postalcode,ld_city,ld_country,ld_language,ld_email,ld_telephone,ld_telephone2,ld_type,ld_passwordchanged,ld_passwordexpires,ld_source,ld_quota,ld_quotacount)
values     (1,CURRENT_TIMESTAMP,0,1,'admin','d033e22ae348aeb566fc214aec3585c4da997','Admin','Admin','','','','','en','admin@admin.net','','',0,null,0,0,-1,0);
insert into ld_group
values     (-1,CURRENT_TIMESTAMP,0,'_user_1','',1);
insert into ld_usergroup
values (1,1);
insert into ld_usergroup
values (-1,1);

insert into ld_menugroup values (2,4,0);
insert into ld_menugroup values (14,4,0);
insert into ld_menugroup values (25,4,0);


insert into ld_folder (ld_id,ld_lastmodified,ld_deleted,ld_name,ld_parentid,ld_type,ld_creation)
values (5,CURRENT_TIMESTAMP,0,'/',5,0,CURRENT_TIMESTAMP);
insert into ld_foldergroup(ld_folderid, ld_groupid, ld_write , ld_add, ld_security, ld_immutable, ld_delete, ld_rename, ld_import, ld_export, ld_sign, ld_archive, ld_workflow, ld_download)
values (5,2,1,1,0,0,1,1,0,0,0,0,0,1);
insert into ld_foldergroup(ld_folderid, ld_groupid, ld_write , ld_add, ld_security, ld_immutable, ld_delete, ld_rename, ld_import, ld_export, ld_sign, ld_archive, ld_workflow, ld_download)
values (5,3,0,0,0,0,0,0,0,0,0,0,0,1);
insert into ld_foldergroup(ld_folderid, ld_groupid, ld_write , ld_add, ld_security, ld_immutable, ld_delete, ld_rename, ld_import, ld_export, ld_sign, ld_archive, ld_workflow, ld_download)
values (5,4,1,1,0,0,1,1,0,0,0,0,0,1);