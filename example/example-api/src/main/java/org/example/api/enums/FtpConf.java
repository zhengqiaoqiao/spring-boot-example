package org.example.api.enums;

public enum FtpConf {
	
	/**图片 **/
	IMAGE {
		public String getPath() {
			return "soft";
		}

		public Integer getMaxSize() {
			return 2 * 1024 * 1024;
		}
	},
	/**WORD **/
	WORD {
		public String getPath() {
			return "/word";
		}

		public Integer getMaxSize() {
			return 2 * 1024 * 1024;
		}
	},
	/**EXCEL**/
	EXCEL {
		public String getPath() {
			return "/excel";
		}

		public Integer getMaxSize() {
			return 2 * 1024 * 1024;
		}
	},
	/**VIDEO**/
	VIDEO {
		public String getPath() {
			return "/video";
		}

		public Integer getMaxSize() {
			return 20 * 1024 * 1024;
		}
	};
	public abstract String getPath();

	public abstract Integer getMaxSize();
	
}
