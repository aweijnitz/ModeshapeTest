package awe.modeshape;

import org.modeshape.jcr.ModeShapeEngine;
import org.modeshape.jcr.RepositoryConfiguration;
import org.modeshape.common.collection.Problems;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.Node;
import javax.jcr.LoginException;
import javax.jcr.RepositoryException;


/**
 * Modeshape "Hello world!"
 * Class used as testbed for Modeshape.
 *
 */
public class App {

	private Repository repository;

	public App(Repository repository) {
		this.repository = repository;

	}

	protected void printRootNode() 
		throws LoginException, RepositoryException {
		Session session = repository.login("default");
		 
		// Get the root node ...
		Node root = session.getRootNode();
		assert root != null;
		 
		System.out.println("Found the root node in the \"" + session.getWorkspace().getName() + "\" workspace");
		 
		session.logout();
	}


	/** Deploy a repository and run some operations.
	*/
    public static void main( String[] args ) {
    
        System.out.println("Starting ModeShapeEngine");

		// Create and start the engine ...
		ModeShapeEngine engine = new ModeShapeEngine();
		engine.start();

		if(args.length == 0) {
			System.out.println("Usage MyRep <path to repository config file>");
			return;
		}
		try {
			RepositoryConfiguration config = RepositoryConfiguration.read(args[0]);

			// Check config before deployment
			Problems problems = config.validate();
			if (problems.hasErrors()) {
			    System.err.println("Problems starting the engine.");
			    System.err.println(problems);
			    return;
			}

			Repository repository = engine.deploy(config);

			App repo = new App(repository);
			repo.printRootNode();

		} catch(Exception e) {
			e.printStackTrace();
		}
    }
}
