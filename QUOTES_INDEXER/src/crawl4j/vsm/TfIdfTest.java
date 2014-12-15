package crawl4j.vsm;

public class TfIdfTest {
	//http://www.seo-camp.org/wp-content/uploads/2009/02/apport_semantique.pdf
	public static void main(String[] args){
		CorpusCache.load();
		String text1 = "the cat goes out the garden into the forest";
		String text2 = "the wolf is stalking rabits in the garden and in the woods";
		System.out.println("TF similarity" + CorpusCache.computeTFSimilarity(text1, text2));
		System.out.println("TFIDF similarity" + CorpusCache.computeTFSIDFimilarity(text1, text2));

		System.out.println("TF similarity" + CorpusCache.computeTFSimilarity(text2, text1));
		System.out.println("TFIDF similarity" + CorpusCache.computeTFSIDFimilarity(text2, text1));
		
		System.out.println("TF similarity" + CorpusCache.computeTFSimilarity(text2, text2));
		System.out.println("TFIDF similarity" + CorpusCache.computeTFSIDFimilarity(text2, text2));
		
		System.out.println("TF similarity" + CorpusCache.computeTFSimilarity(text1, text1));
		System.out.println("TFIDF similarity" + CorpusCache.computeTFSIDFimilarity(text1, text1));
	
	}
}
		

