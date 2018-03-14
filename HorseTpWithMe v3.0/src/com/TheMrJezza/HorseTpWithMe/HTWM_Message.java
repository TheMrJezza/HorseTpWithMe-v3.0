package com.TheMrJezza.HorseTpWithMe;

public enum HTWM_Message {
	BLOCKED_WORLD {
		@Override
		public String toString() {
			return Main.getInstance().getSettings().blockedWorldMessage;
		}
	},
	NO_PERMISSION {
		@Override
		public String toString() {
			return Main.getInstance().getSettings().noPermissionMessage;
		}
	},
	ECONOMY_SUCCESS {
		@Override
		public String toString() {
			return Main.getInstance().getSettings().economySuccessMessage;
		}
	},
	TOO_MANY_ON_LEAD {
		@Override
		public String toString() {
			return Main.getInstance().getSettings().tooManyOnLeadMessage;
		}
	},
	INSUFFICIENT_FUNDS {
		@Override
		public String toString() {
			return Main.getInstance().getSettings().economyInsufficientFundsMessage;
		}
	}, BLOCKED_AREA {
		@Override
		public String toString() {
			return Main.getInstance().getSettings().areaIsBlocked;
		}
	}
}
