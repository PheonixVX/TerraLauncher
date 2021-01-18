package io.github.PheonixVX.TerraLauncher.version;

import java.io.IOException;

public interface AbstractVersionDownload {
	void downloadClient();
	void downloadLibraries();
	void downloadNatives();
	void launch() throws IOException;
}
