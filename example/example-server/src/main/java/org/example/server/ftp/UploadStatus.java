package org.example.server.ftp;

public enum UploadStatus {
	Create_Directory_Fail,
	File_Exits,
	Remote_Bigger_Local,
	Upload_From_Break_Failed,
	Delete_Remote_Faild,
	Create_Directory_Success,
	Upload_From_Break_Success,
	Upload_New_File_Success,
	Upload_New_File_Failed
}
