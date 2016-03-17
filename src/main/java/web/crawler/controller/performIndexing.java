package web.crawler.controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharReader;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.charfilter.HTMLStripCharFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.search.DefaultSimilarity;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import web.crawler.db.dao.DocDao;
import web.crawler.db.dao.IndexDao;
import web.crawler.db.model.Index;
import web.crawler.db.model.WordDoc;

public class performIndexing {
	private int fileCount = 0;

	performIndexing() {

	}

	public void luceneIndexing(String path, IndexWriter writer)
			throws IOException {

		Version version = Version.LUCENE_36;
		Analyzer an = new StandardAnalyzer(version);
		File root = new File(path);
		File[] list = root.listFiles();
		DocDao docDao = new DocDao();
		if (list == null)
			return;

		for (File f : list) {
			if (f.isDirectory()) {
				String absPath = f.getAbsolutePath();
				luceneIndexing(absPath, writer);
			} else {
				System.out.println("File Counter:" + (++fileCount));
				Document doc = new Document();
				Reader r = new FileReader(f);
				HTMLStripCharFilter filter = new HTMLStripCharFilter(
						CharReader.get(new FileReader(f)));
				TokenStream ts = an.tokenStream("html", filter);

				doc.add(new Field("html", ts, Field.TermVector.WITH_POSITIONS));
				doc.add(new Field("path", f.getAbsolutePath(), Field.Store.YES,
						Field.Index.NO));
				doc.add(new Field("title", docDao.getDocByPath(
						f.getAbsolutePath()).getTitle(), Field.Store.YES,
						Field.Index.ANALYZED));
				writer.addDocument(doc);

			}
		}

	}

	int getNoOfTermsInDoc(IndexReader reader, int docno) throws IOException {
		TermFreqVector[] tfv = reader.getTermFreqVectors(docno);
		String[] arr = tfv[0].getTerms();

		return arr.length;
	}

	public void calculateTFIDF(String path) throws CorruptIndexException,
			IOException {
		DefaultSimilarity simi = new DefaultSimilarity();
		
		
		File indexDirectory = new File("D:/webcrawler/Indexing");
		IndexReader reader = IndexReader.open(FSDirectory.open(indexDirectory));
		System.out.println("Total Number of documents in the index:"
				+ reader.numDocs());
		TermEnum enumeration = reader.terms();
		
		List<Index> indxList = new ArrayList<Index>();
		IndexDao indexDao = new IndexDao();
		while (enumeration.next()) {
			// get all the terms and store the doc path,tfidf,link analysis
			// score
			TermDocs docEnumerationForTerm = reader
					.termDocs(enumeration.term());
			String term=enumeration.term().text();

			Index index = new Index();

			List<WordDoc> wordDocList = new ArrayList<WordDoc>();
			while (docEnumerationForTerm.next()) {
				WordDoc wd = new WordDoc();
				int documentNumber = (docEnumerationForTerm.doc());
				int termFrequency = docEnumerationForTerm.freq();

				int documentFrequencyOfTerm = reader
						.docFreq(enumeration.term());

				int termsInDoc = getNoOfTermsInDoc(reader, documentNumber);

				// get total no of terms in the document
				double noramlizedTf = (double) termFrequency
						/ (double) termsInDoc;

				double idf = simi
						.idf(documentFrequencyOfTerm, reader.numDocs());

				double tfidf = noramlizedTf * idf;

				Document docObject = reader.document(documentNumber);
				String documentPath = docObject.get("path");
				System.out.println(documentPath + " " + tfidf + " ");
				wd.setDocHash(documentPath);
				wd.setTerm(enumeration.term().text());
				wd.setIdf(idf);
				wd.setTf(noramlizedTf);
				wd.setTfIdf(tfidf);
				wordDocList.add(wd);
				
			}
			index.setTerm(term);
			index.setDocuments(wordDocList);
			indxList.add(index);
			if(indxList.size()==500){
				indexDao.saveIndex(indxList);
				indxList.clear();
			}
		}
		indexDao.saveIndex(indxList);
		System.out.println("Indexing Finished");
	}
}
