DELETE FROM notice.notice;
DELETE FROM notice.notice_fs;

INSERT INTO notice.notice(id, title, department_id, content, category, created_at, updated_at, views) VALUES
    (1,'공지사항1',1,'1번 공지 내용입니다!!','ACADEMICS','1000-01-01 00:00:00','1000-01-01 00:00:00',0),
    (2,'공지사항2',2,'2번 공지 내용입니다!!','SUBSCRIPTION','1000-01-01 00:00:00','1000-01-01 00:00:00',0),
    (3,'공지사항3',3,'3번 공지 내용입니다!!','SCHOLARSHIP','1000-01-01 00:00:00','1000-01-01 00:00:00',0),
    (4,'공지사항4',4,'4번 공지 내용입니다!!','INTERNATIONAL_EXCHANGE','1000-01-01 00:00:00','1000-01-01 00:00:00',0),
    (5,'공지사항4',5,'5번 공지 내용입니다!!','INTERNATIONAL_EXCHANGE','1000-01-01 00:00:00','1000-01-01 00:00:00',0);

INSERT INTO notice.notice_fs(id, title, content, category,url, created_at, updated_at,views) VALUES
    (1,'공지사항1','1번 공지 내용입니다!!','CERTIFICATION','http://google.com','1000-01-01 00:00:00','1000-01-01 00:00:00',0),
    (2,'공지사항2','2번 공지 내용입니다!!','SUBSCRIPTION','http://google.com','1000-01-01 00:00:00','1000-01-01 00:00:00',0),
    (3,'공지사항3','3번 공지 내용입니다!!','CERTIFICATION','http://google.com','1000-01-01 00:00:00','1000-01-01 00:00:00',0),
    (4,'공지사항4','4번 공지 내용입니다!!','EXPERIENTIAL_ACTIVITIES','http://google.com','1000-01-01 00:00:00','1000-01-01 00:00:00',0);