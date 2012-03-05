package git

import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit

/**
 *
 */
class Commit {
    Repository repository
    RevCommit revCommit

    Commit(Repository repository, RevCommit revCommit) {
        assert repository
        assert revCommit
        this.repository = repository
        this.revCommit = revCommit
    }


    Tree getTree() {
        new Tree(repository, revCommit.tree)
    }


    String getMessage() {
        revCommit.fullMessage
    }

}
