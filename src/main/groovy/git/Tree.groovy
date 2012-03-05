package git

import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevTree
import org.eclipse.jgit.treewalk.CanonicalTreeParser
import org.eclipse.jgit.treewalk.TreeWalk

/**
 *
 */
class Tree {
    Repository repository
    RevTree revTree


    Tree(Repository repository, RevTree revTree) {
        assert repository
        assert revTree
        this.repository = repository
        this.revTree = revTree
    }


    def getPaths() {
        TreeWalk treeWalk = new TreeWalk(repository)
        treeWalk.addTree(revTree)
        treeWalk.recursive = true

        CanonicalTreeParser canonicalTreeParser = treeWalk.getTree(0, CanonicalTreeParser)

        List<String> paths = []

        while (!canonicalTreeParser.eof()) {
            paths << canonicalTreeParser.entryPathString
            canonicalTreeParser.next()
        }
        paths
    }

}
