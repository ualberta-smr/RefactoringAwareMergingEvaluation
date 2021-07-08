package ca.ualberta.cs.smr.evaluation.database;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("conflicting_file")
public class ConflictingFile extends Model {

    public ConflictingFile() {}

    public ConflictingFile(MergeResult mergeResult, ConflictingFileData conflictingFileData) {
        set("merge_tool", mergeResult.getMergeTool(), "path", conflictingFileData.getFilePath(),
                "total_conflicts", conflictingFileData.getConflictingBlocks(),
                "total_conflicting_loc", conflictingFileData.getConflictingLOC(),
                "merge_result_id", mergeResult.getId(),
                "merge_commit_id", mergeResult.getMergeCommitId(),
                "project_id", mergeResult.getProjectId());
    }
}
