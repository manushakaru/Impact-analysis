import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import model.ClassEntity;
import model.MethodEntity;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.FileTreeIterator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HighlightAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        System.out.println(anActionEvent.getProject().getBasePath()+"/.git");
        try {
            Repository existingRepo = new FileRepositoryBuilder()
                    .setGitDir(new File(anActionEvent.getProject().getBasePath()+"/.git"))
                    .build();

            Git git = new Git(existingRepo);

            List<Ref> branches = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
            RevCommit youngestCommit=null;
                RevWalk walk = new RevWalk(git.getRepository());
                for(Ref branch : branches) {
                    RevCommit commit = walk.parseCommit(branch.getObjectId());
                    if (youngestCommit == null || commit.getAuthorIdent().getWhen().compareTo(
                            youngestCommit.getAuthorIdent().getWhen()) > 0)
                        youngestCommit = commit;
                }

            DiffFormatter formatter = new DiffFormatter( System.out );
            formatter.setRepository( git.getRepository() );
            AbstractTreeIterator commitTreeIterator = prepareTreeParser( git.getRepository(),  youngestCommit );
            FileTreeIterator workTreeIterator = new FileTreeIterator( git.getRepository() );
            List<DiffEntry> diffEntries = formatter.scan( commitTreeIterator, workTreeIterator );

            for( DiffEntry entry : diffEntries ) {
                System.out.println( "Entry: " + entry + ", from: " + entry.getOldId() + ", to: " + entry.getNewId() );
                formatter.format( entry );
            }
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static AbstractTreeIterator prepareTreeParser(Repository repository, RevCommit commit) throws Exception {
        RevWalk walk = new RevWalk(repository);
        RevTree tree = walk.parseTree(commit.getTree().getId());

        CanonicalTreeParser oldTreeParser = new CanonicalTreeParser();
        ObjectReader oldReader = repository.newObjectReader();
        try {
            oldTreeParser.reset(oldReader, tree.getId());
        } finally {

        }
        return oldTreeParser;
    }


}
