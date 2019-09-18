package io.weidongxu.util.releaseautomation;

import java.io.File;
import java.io.IOException;

public interface Release {

    void processReadme(File readmeFile) throws IOException;

    void processPrepareNote(File prepareNoteFile) throws IOException;

    void processReadme(String readmeFilename) throws IOException;

    void processPrepareNote(String prepareNoteFilename) throws IOException;
}
