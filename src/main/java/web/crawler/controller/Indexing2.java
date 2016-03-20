package web.crawler.controller;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import web.crawler.constant.Paths;

public class Indexing2 {
	public static void main(String sr[]) throws IOException {
		Version version = Version.LUCENE_36;
		Analyzer an = new StandardAnalyzer(version);
		Directory index = FSDirectory.open(new File(Paths.PATH_TO_STORE_INDEXING));
		IndexWriterConfig config = new IndexWriterConfig(version, an);
		boolean createNewIndex = true;
		if (createNewIndex) {

			config.setOpenMode(OpenMode.CREATE);
		} else {
			config.setOpenMode(OpenMode.CREATE_OR_APPEND);
		}
			IndexWriter writer = new IndexWriter(index, config);
			performIndexing writeIndex=new performIndexing();
			writeIndex.luceneIndexing(Paths.PATH_TO_STORE_CRAWLED_DATA, writer);
			writer.close();
			writeIndex.calculateTFIDF(Paths.PATH_TO_STORE_INDEXING);
	}
	

}
