import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.AbstractVcs;
import com.intellij.openapi.vcs.ProjectLevelVcsManager;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.openapi.vcs.changes.ContentRevision;
import com.intellij.openapi.vcs.changes.LocalChangeList;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GitVcs {

    public void getCurrentBranch(Project project) {
        AbstractVcs[] VCS = ProjectLevelVcsManager.getInstance(project).getAllActiveVcss();
        if (VCS.length == 0) {

        }

        AbstractVcs currentVersionControl = VCS[0];
        //VCS[0].getChangeProvider().getChanges();
    }

    @NotNull
    public static List<VirtualFile> getAffectedFiles(String changeListName, Project project) {
        final ChangeListManager changeListManager = ChangeListManager.getInstance(project);
        if (changeListName == null) {
            return changeListManager.getAffectedFiles();
        }
        //final LocalChangeList changeList = changeListManager.findChangeList(changeListName);
        final LocalChangeList changeList = changeListManager.getChangeLists().get(0);
        if (changeList != null) {
            List<VirtualFile> files = new ArrayList<VirtualFile>();
            System.out.println(changeList.getChanges().size());
            for (Change change : changeList.getChanges()) {
                final ContentRevision afterRevision = change.getAfterRevision();
                if (afterRevision != null) {
                    final VirtualFile file = afterRevision.getFile().getVirtualFile();
                    if (file != null) {
                        files.add(file);
                    }

                    try {
                        System.out.println(afterRevision.getContent());
                    } catch (VcsException e) {
                        e.printStackTrace();
                    };
                }
            }
            return files;
        }

        return Collections.emptyList();
    }

}
