package com.logicaldoc.core.document.dao;

import java.util.Collection;

import com.logicaldoc.core.AbstractCoreTestCase;
import com.logicaldoc.core.document.Document;
import com.logicaldoc.core.document.Version;
import com.logicaldoc.core.security.Menu;
import com.logicaldoc.core.security.dao.MenuDAO;

/**
 * Test case for <code>HibernateDocumentDAO</code>
 * 
 * @author Marco Meschieri
 * @version $Id:$
 * @since 3.0
 */
public class HibernateDocumentDAOTest extends AbstractCoreTestCase {

	// Instance under test
	private DocumentDAO dao;

	private MenuDAO menuDao;

	public HibernateDocumentDAOTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();

		// Retrieve the instance under test from spring context. Make sure that
		// it is an HibernateDocumentDAO
		dao = (DocumentDAO) context.getBean("DocumentDAO");
		menuDao = (MenuDAO) context.getBean("MenuDAO");
	}

	public void testDelete() {
		assertTrue(dao.delete(100));
		Document doc = dao.findByPrimaryKey(100);
		assertNull(doc);
	}

	public void testFindAll() {
		Collection<Document> documents = dao.findAll();
		assertNotNull(documents);
		assertEquals(2, documents.size());
	}

	public void testFindByPrimaryKey() {
		Document doc = dao.findByPrimaryKey(1);
		assertNotNull(doc);
		assertEquals(1, doc.getId());
		assertEquals("testDocname", doc.getTitle());
		assertEquals(2, doc.getVersions().size());
		assertNotNull(doc.getFolder());
		assertEquals(103, doc.getFolder().getMenuId());

		// Try with unexisting document
		doc = dao.findByPrimaryKey(99);
		assertNull(doc);
	}

	public void testFindByUserName() {
		Collection<Long> ids = dao.findByUserName("sebastian");
		assertNotNull(ids);
		assertEquals(2, ids.size());
		assertTrue(ids.contains(new Long(2)));

		// Try with a user without documents
		ids = dao.findByUserName("test");
		assertNotNull(ids);
		assertEquals(0, ids.size());
	}

	public void testFindByFolder() {
		Collection<Long> ids = dao.findByFolder(103);
		assertNotNull(ids);
		assertEquals(2, ids.size());
		assertTrue(ids.contains(new Long(2)));

		ids = dao.findByFolder(1111);
		assertNotNull(ids);
		assertEquals(0, ids.size());
	}
	
	public void testFindLastModifiedByUserName() {
		Collection<Document> coll = dao.findLastModifiedByUserName("author", 10);
		assertNotNull(coll);
		assertEquals(2, coll.size());

		coll = dao.findLastModifiedByUserName("sebastian", 10);
		assertNotNull(coll);
		assertEquals(1, coll.size());
	}

	public void testFindDocIdByKeyword() {
		Collection<Long> ids = dao.findDocIdByKeyword("abc");
		assertNotNull(ids);
		assertEquals(1, ids.size());
		assertEquals(new Long(1), ids.iterator().next());

		ids = dao.findDocIdByKeyword("xxx");
		assertNotNull(ids);
		assertEquals(0, ids.size());
	}

	public void testStore() {
		Document doc = new Document();
		Menu menu = menuDao.findByPrimaryKey(Menu.MENUID_HOME);
		doc.setFolder(menu);
		doc.setPublisher("admin");
		doc.setTitle("test");
		doc.addKeyword("pippo");
		doc.addKeyword("pluto");
		// Try a long keyword
		doc.addKeyword("123456789123456789123456789");
		Version version = new Version();
		version.setVersion("1.0");
		version.setComment("comment");
		doc.addVersion(version);
		assertTrue(dao.store(doc));
		assertEquals(3, doc.getId());
		doc = dao.findByPrimaryKey(3);
		assertNotNull(doc);
		assertEquals(3, doc.getId());
		assertEquals(3, doc.getKeywords().size());
		assertTrue(doc.getKeywords().contains("pluto"));
		assertTrue(doc.getKeywords().contains("123456789123456789123456789"));
		assertEquals(1, doc.getVersions().size());
		assertEquals(version, doc.getVersion("1.0"));

		// Try to change the version comment
		doc = dao.findByPrimaryKey(3);
		version = doc.getVersion("1.0");
		version.setComment("xxxx");
		version.setVersion("1.0");
		doc.clearVersions();
		doc.addVersion(version);
		dao.store(doc);
		doc = dao.findByPrimaryKey(3);
		version = doc.getVersion("1.0");
		assertEquals("xxxx", version.getComment());

		// Load an existing document and modify it
		doc = dao.findByPrimaryKey(1);
		assertNotNull(doc);
		assertEquals("testDocname", doc.getTitle());
		assertEquals(2, doc.getVersions().size());
		assertEquals(3, doc.getKeywords().size());
		doc.setTitle("xxxx");
		assertTrue(dao.store(doc));
		doc = dao.findByPrimaryKey(1);
		assertNotNull(doc);
		assertEquals(1, doc.getId());
		assertEquals("xxxx", doc.getTitle());
		assertEquals(2, doc.getVersions().size());
		assertEquals(3, doc.getKeywords().size());
	}

	public void testToKeywords() {
		Collection<String> coll = dao.toKeywords("my name is tom");
		assertNotNull(coll);
		assertEquals(2, coll.size());

		coll = dao.toKeywords("il mio nome e' tom");
		assertNotNull(coll);
		assertEquals(3, coll.size());

		coll = dao.toKeywords("il mio nome e' 123456789123456789123456789");
		assertNotNull(coll);
		System.out.println(coll);
		assertEquals(3, coll.size());
		assertFalse(coll.contains("123456789123456789123456789"));
		assertTrue(coll.contains("12345678912345678912"));
	}

	public void testFindKeywords() {
		Collection<String> keywords = dao.findKeywords("a", "admin");
		assertNotNull(keywords);
		assertEquals(1, keywords.size());
		assertEquals("abc", keywords.iterator().next());
	}
	
	public void testFindDocIdByUsernameAndKeyword() {
		Collection<Long> ids = dao.findDocIdByUsernameAndKeyword("admin", "abc");
		assertNotNull(ids);
		assertEquals(1, ids.size());
		assertEquals(new Long(1), ids.iterator().next());

		ids = dao.findDocIdByUsernameAndKeyword("admin", "xxx");
		assertNotNull(ids);
		assertEquals(0, ids.size());
		
		ids = dao.findDocIdByUsernameAndKeyword("xxx", "abc");
		assertNotNull(ids);
		assertEquals(0, ids.size());
	}
	
	public void testFindLastDownloadsByUserName() {
		Collection<Document> documents = dao.findLastDownloadsByUserName("admin", 10);
		assertNotNull(documents);
		assertEquals(2, documents.size());
	}
}