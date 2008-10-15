CREATE TABLE CO_ACCOUNT (
  CO_ACCOUNTID         INT   NOT NULL,
  CO_USERNAME          VARCHAR(255),
  CO_MAILADDRESS       VARCHAR(255),
  CO_PROVIDER          VARCHAR(255),
  CO_HOST              VARCHAR(255),
  CO_PORT              VARCHAR(255),
  CO_ACCOUNTUSER       VARCHAR(255),
  CO_ACCOUNTPASSWORD   VARCHAR(255),
  CO_ALLOWEDTYPES      VARCHAR(255),
  CO_DELETEFROMMAILBOX INT,
  CO_LANGUAGE          VARCHAR(255),
  CO_ENABLED           INT,
  CO_TARGETFOLDER      INT,
    PRIMARY KEY ( CO_ACCOUNTID ));

CREATE TABLE CO_ATTACHMENT (
  CO_MESSAGEID INT   NOT NULL,
  CO_FILENAME  VARCHAR(255),
  CO_ICON      VARCHAR(255),
  CO_MIMETYPE  VARCHAR(255),
  CO_PARTID    INT   NOT NULL,
    PRIMARY KEY ( CO_MESSAGEID,CO_PARTID ));

CREATE TABLE CO_EMAIL (
  CO_MESSAGEID     INT   NOT NULL,
  CO_ACCOUNTID     INT,
  CO_EMAILID       VARCHAR(255),
  CO_MESSAGETEXT   VARCHAR(255),
  CO_AUTHOR        VARCHAR(30),
  CO_SUBJECT       VARCHAR(255),
  CO_SENTDATE      VARCHAR(20),
  CO_RED           INT,
  CO_AUTHORADDRESS VARCHAR(255),
  CO_USERNAME      VARCHAR(30),
  CO_FOLDER        VARCHAR(30),
    PRIMARY KEY ( CO_MESSAGEID ));

CREATE TABLE CO_GROUPS (
  CO_GROUPNAME VARCHAR(255)   NOT NULL,
  CO_GROUPDESC VARCHAR(255),
    PRIMARY KEY ( CO_GROUPNAME ));

CREATE TABLE CO_MENUGROUP (
  CO_MENUID      INT   NOT NULL,
  CO_GROUPNAME   VARCHAR(30),
  CO_WRITEENABLE INT);

CREATE TABLE CO_MENUS (
  CO_MENUID     INT   NOT NULL,
  CO_MENUTEXT   VARCHAR(100),
  CO_MENUPARENT INT,
  CO_MENUSORT   INT,
  CO_MENUICON   VARCHAR(255),
  CO_MENUPATH   VARCHAR(255),
  CO_MENUTYPE   INT,
  CO_MENUHIER   INT,
  CO_MENUREF    VARCHAR(255),
  CO_MENUSIZE   BIGINT,
    PRIMARY KEY ( CO_MENUID ));

CREATE TABLE CO_RECIPIENT (
  CO_MESSAGEID INT   NOT NULL,
  CO_ADDRESS   VARCHAR(255)   NOT NULL,
  CO_NAME      VARCHAR(255),
    PRIMARY KEY ( CO_MESSAGEID,CO_ADDRESS ));

CREATE TABLE CO_SYSTEMMESSAGE (
  CO_MESSAGEID    INT   NOT NULL,
  CO_AUTHOR       VARCHAR(100),
  CO_RECIPIENT    VARCHAR(100),
  CO_MESSAGETEXT  VARCHAR(2000),
  CO_SUBJECT      VARCHAR(255),
  CO_SENTDATE     VARCHAR(20)   NOT NULL,
  CO_DATESCOPE    INT,
  CO_PRIO         INT,
  CO_CONFIRMATION INT,
  CO_RED          INT   NOT NULL,
    PRIMARY KEY ( CO_MESSAGEID ));

CREATE TABLE CO_USERGROUP (
  CO_GROUPNAME VARCHAR(255)   NOT NULL,
  CO_USERNAME  VARCHAR(255)   NOT NULL,
    PRIMARY KEY ( CO_GROUPNAME,CO_USERNAME ));

CREATE TABLE CO_USERS (
  CO_USERNAME   VARCHAR(255)   NOT NULL,
  CO_PASSWORD   VARCHAR(255),
  CO_NAME       VARCHAR(30),
  CO_FIRSTNAME  VARCHAR(30),
  CO_STREET     VARCHAR(100),
  CO_POSTALCODE VARCHAR(10),
  CO_CITY       VARCHAR(30),
  CO_COUNTRY    VARCHAR(30),
  CO_LANGUAGE   VARCHAR(10),
  CO_EMAIL      VARCHAR(255),
  CO_TELEPHONE  VARCHAR(30),
    PRIMARY KEY ( CO_USERNAME ));

CREATE TABLE LD_ARTICLE (
  LD_ARTICLEID   INT   NOT NULL,
  LD_DOCID       BIGINT,
  LD_SUBJECT     VARCHAR(255),
  LD_MESSAGE     VARCHAR(2000),
  LD_DATE TIMESTAMP,
  LD_USERNAME    VARCHAR(30),
    PRIMARY KEY ( LD_ARTICLEID ));

CREATE TABLE LD_DOCUMENT (
  LD_ID           BIGINT   NOT NULL,
  LD_TITLE        VARCHAR(255),
  LD_VERSION      VARCHAR(10),
  LD_DATE         TIMESTAMP,
  LD_PUBLISHER    VARCHAR(30),
  LD_STATUS       INT,
  LD_TYPE         VARCHAR(255),
  LD_CHECKOUTUSER VARCHAR(30),
  LD_SOURCE       VARCHAR(255),
  LD_SOURCEAUTHOR VARCHAR(255),
  LD_SOURCEDATE   TIMESTAMP,
  LD_SOURCETYPE   VARCHAR(255),
  LD_COVERAGE     VARCHAR(255),
  LD_LANGUAGE     VARCHAR(10),
  LD_FOLDERID     INT,
    PRIMARY KEY ( LD_ID ));

CREATE TABLE LD_HISTORY (
  LD_HISTORYID INT   NOT NULL,
  LD_DOCID     BIGINT,
  LD_DATE      TIMESTAMP,
  LO_USERNAME  VARCHAR(30),
  LO_EVENT     VARCHAR(255),
    PRIMARY KEY ( LD_HISTORYID ));

CREATE TABLE LD_KEYWORD (
  LD_DOCID   BIGINT   NOT NULL,
  LD_KEYWORD VARCHAR(255));

CREATE TABLE LD_TERM (
  LD_DOCID     BIGINT   NOT NULL,
  LD_STEM      VARCHAR(255)   NOT NULL,
  LD_VALUE     FLOAT,
  LD_WORDCOUNT INT,
  LD_WORD      VARCHAR(255),
    PRIMARY KEY ( LD_DOCID,LD_STEM ));

CREATE TABLE LD_TICKET (
  LD_TICKETID VARCHAR(255)   NOT NULL,
  LD_DOCID    BIGINT,
  LD_USERNAME VARCHAR(30),
    PRIMARY KEY ( LD_TICKETID ));

CREATE TABLE LD_USERDOC (
  LD_DOCID    BIGINT   NOT NULL,
  LD_USERNAME VARCHAR(255)   NOT NULL,
  LD_DATE     TIMESTAMP,
    PRIMARY KEY ( LD_DOCID,LD_USERNAME ));

CREATE TABLE LD_VERSION (
  LD_DOCID   BIGINT   NOT NULL,
  LD_VERSION VARCHAR(10),
  LD_USER    VARCHAR(30),
  LD_DATE    TIMESTAMP,
  LD_COMMENT VARCHAR(255));

alter table co_account add constraint FK876115A59CC15A2 foreign key (co_targetfolder) references co_menus;
alter table co_attachment add constraint FK5E87E6D6CB751496 foreign key (co_messageid) references co_email;
alter table co_menugroup add constraint FKD58CD46DEA8A715D foreign key (co_menuid) references co_menus;
alter table co_recipient add constraint FK60FEE206CB751496 foreign key (co_messageid) references co_email;
alter table co_usergroup add constraint FK44CA21819E198925 foreign key (co_username) references co_users;
alter table co_usergroup add constraint FK44CA2181B6F18C05 foreign key (co_groupname) references co_groups;
alter table ld_document add constraint FK75ED9C027C565C60 foreign key (ld_folderid) references co_menus;
alter table ld_keyword add constraint FK61BF6A917C693DFD foreign key (ld_docid) references ld_document;
alter table ld_version add constraint FKCC3F49827C693DFD foreign key (ld_docid) references ld_document;




INSERT INTO CO_MENUS
VALUES     (1,
            'db.home',
            0,
            1,
            'home.png',
            '',
            0,
            0,
            NULL,
            0);

INSERT INTO CO_MENUS
VALUES     (2,
            'db.admin',
            1,
            1,
            'administration.png',
            'ROOT',
            1,
            1,
            NULL,
            0);

INSERT INTO CO_MENUS
VALUES     (4,
            'db.personal',
            1,
            3,
            'personal.png',
            'ROOT',
            1,
            1,
            NULL,
            0);

INSERT INTO CO_MENUS
VALUES     (5,
            'db.projects',
            1,
            4,
            'documents.png',
            'ROOT',
            1,
            1,
            'document/browse',
            0);

INSERT INTO CO_MENUS
VALUES     (6,
            'db.user',
            2,
            1,
            'user.png',
            'ROOT/2',
            1,
            2,
            'admin/users',
            0);

INSERT INTO CO_MENUS
VALUES     (7,
            'db.group',
            2,
            2,
            'group.png',
            'ROOT/2',
            1,
            2,
            'admin/groups',
            0);

INSERT INTO CO_MENUS
VALUES     (8,
            'db.logging',
            2,
            3,
            'logging.png',
            'ROOT/2',
            1,
            2,
            'admin/logs',
            0);

INSERT INTO CO_MENUS
VALUES     (13,
            'db.messages',
            4,
            1,
            'message.png',
            'ROOT/4',
            1,
            2,
            'communication/messages',
            0);

INSERT INTO CO_MENUS
VALUES     (16,
            'db.changepassword',
            4,
            3,
            'password.png',
            'ROOT/4',
            1,
            2,
            'settings/password',
            0);

INSERT INTO CO_MENUS
VALUES     (17,
            'directory',
            2,
            5,
            'open.png',
            'ROOT/2',
            1,
            2,
            'admin/folders',
            0);

INSERT INTO CO_MENUS
VALUES     (19,
            'db.editme',
            4,
            4,
            'user.png',
            'ROOT/4',
            1,
            2,
            'settings/personalData',
            0);

INSERT INTO CO_MENUS
VALUES     (20,
            'db.emails',
            2,
            4,
            'mail.png',
            'ROOT/2',
            1,
            2,
            NULL,
            0);

INSERT INTO CO_MENUS
VALUES     (23,
            'smtp',
            20,
            3,
            'mail_preferences.png',
            'ROOT/2/20',
            1,
            3,
            'admin/smtp',
            0);

INSERT INTO CO_MENUS
VALUES     (24,
            'db.emailaccounts',
            20,
            4,
            'mailbox.png',
            'ROOT/2/20',
            1,
            3,
            'admin/accounts',
            0);

INSERT INTO CO_MENUS
VALUES     (25,
            'db.searchengine',
            2,
            6,
            'search.png',
            'ROOT/2',
            1,
            2,
            'admin/searchEngine',
            0);

INSERT INTO CO_MENUS
VALUES     (26,
            'db.keywords',
            1,
            5,
            'keywords.png',
            'ROOT',
            1,
            1,
            'search/keywords',
            0);

INSERT INTO CO_MENUS
VALUES     (27,
            'db.backup',
            2,
            7,
            'backup.png',
            'ROOT/2',
            1,
            2,
            'admin/backup',
            0);

INSERT INTO CO_MENUS
VALUES     (14,
            'task.tasks',
            2,
            8,
            'thread.png',
            'ROOT/2',
            1,
            2,
            'admin/tasks',
            0);

INSERT INTO CO_GROUPS
VALUES     ('admin',
            'Group of admins');

INSERT INTO CO_GROUPS
VALUES     ('author',
            'Group of authors');

INSERT INTO CO_GROUPS
VALUES     ('guest',
            'Group of guest');

INSERT INTO CO_USERS
           (CO_USERNAME,
            CO_PASSWORD,
            CO_NAME,
            CO_FIRSTNAME,
            CO_STREET,
            CO_POSTALCODE,
            CO_CITY,
            CO_COUNTRY,
            CO_LANGUAGE,
            CO_EMAIL,
            CO_TELEPHONE)
VALUES     ('admin',
            'd033e22ae348aeb566fc214aec3585c4da997',
            'Admin',
            'Admin',
            '',
            '',
            '',
            '',
            'en',
            'admin@admin.net',
            '');

INSERT INTO CO_MENUGROUP
VALUES     (1,
            'admin',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (2,
            'admin',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (4,
            'admin',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (5,
            'admin',
            1);

INSERT INTO CO_MENUGROUP
VALUES     (6,
            'admin',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (7,
            'admin',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (8,
            'admin',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (13,
            'admin',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (14,
            'admin',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (16,
            'admin',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (17,
            'admin',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (19,
            'admin',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (20,
            'admin',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (23,
            'admin',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (24,
            'admin',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (25,
            'admin',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (26,
            'admin',
            1);

INSERT INTO CO_MENUGROUP
VALUES     (27,
            'admin',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (1,
            'author',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (4,
            'author',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (5,
            'author',
            1);

INSERT INTO CO_MENUGROUP
VALUES     (13,
            'author',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (16,
            'author',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (19,
            'author',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (20,
            'author',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (23,
            'author',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (24,
            'author',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (26,
            'author',
            1);

INSERT INTO CO_MENUGROUP
VALUES     (1,
            'guest',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (4,
            'guest',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (5,
            'guest',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (13,
            'guest',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (16,
            'guest',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (19,
            'guest',
            0);

INSERT INTO CO_MENUGROUP
VALUES     (26,
            'guest',
            0);
