package kr.hhplus.be.server.interfaces.bestItems;

import kr.hhplus.be.server.application.bestItem.BestItemsCacheDto;
import lombok.Getter;

import java.util.List;


public class BestItemResponse {

	@Getter
	public static class BestItems {
		private List<BestItemsCacheDto> cacheList;

		private BestItems(List<BestItemsCacheDto> cacheList) {
			this.cacheList = cacheList;
		}

		public static BestItems of(List<BestItemsCacheDto> cacheList) {
			return new BestItems(cacheList);
		}
	}
}
