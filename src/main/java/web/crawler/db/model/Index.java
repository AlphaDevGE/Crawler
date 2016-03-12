package web.crawler.db.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import web.crawler.constant.DBTable;

@Document(collection = DBTable.INDEX)
public class Index {	
	@Id
	private String id;
	
	@Indexed
	private String term;
	private List<WordDoc> documents;
	
	public Index(){ super(); }
	
	public Index(String term, List<WordDoc> documents) {
		super();
		this.term = term.toLowerCase();
		if(documents != null)
		{
			for(WordDoc wd: documents)
			{
				if( wd.getTerm() == null )
					wd.setTerm(term);
				else if(!term.equals(wd.getTerm()))
				{
					System.out.println("!!!!! Warning !!!!!");
					System.out.println("The term: '"+ wd.getTerm() + "' in List<WordDoc> not EQUAL to the term: '" 
					+ term +"' that is being Indexed.");
					System.out.println("The term: '"+ wd.getTerm() + "' in List<WordDoc> changed to : '" + term + "'");
					wd.setTerm(term);
				}
			}
		}
		this.documents = documents;
	}
	
	public void addWordDoc(WordDoc wd){	
		if(documents == null)
			documents = new ArrayList<WordDoc>();
		if(wd == null)
			System.out.println("Null WordDoc cannot be assigned to the collection!!!!! ");
		else if( wd.getTerm() == null )
			wd.setTerm(term);
		else if(!term.equals(wd.getTerm()))
		{
			System.out.println("!!!!! Warning !!!!!");
			System.out.println("The term: '"+ wd.getTerm() + "' in WordDoc not EQUAL to the term: '" 
			+ term +"' that is being Indexed.");
			System.out.println("The term: '"+ wd.getTerm() + "' in WordDoc changed to : '" + term + "'");
			wd.setTerm(term);
		}
		documents.add(wd);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public List<WordDoc> getDocuments() {
		return documents;
	}

	public void setDocuments(List<WordDoc> documents) {
		if(documents != null)
		{
			for(WordDoc wd: documents)
			{
				if( wd.getTerm() == null )
					wd.setTerm(term);
				else if(!term.equals(wd.getTerm()))
				{
					System.out.println("!!!!! Warning !!!!!");
					System.out.println("The term: '"+ wd.getTerm() + "' in List<WordDoc> not EQUAL to the term: '" 
					+ term +"' that is being Indexed.");
					System.out.println("The term: '"+ wd.getTerm() + "' in List<WordDoc> changed to : '" + term + "'");
					wd.setTerm(term);
				}
			}
			this.documents = documents;
		}
		else
			System.out.println(" Null cannot be assigned to the List<WordDoc> collection");
		
	}
	
	public String toString() {
		String str = String.format(
                "'Index':{\n"
              + "		'id': %s,\n"
              + "		'term': '%s',\n"
              + "		'documents': '%s',\n"
              + "		},\n",
                id, term, documents
                );
		return str;
	}
	
}