package kr.hhplus.be.server.application.product;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductCriteria {

	@Getter
	public static class TopOrderedProducts {
		private LocalDateTime startDate;
		private LocalDateTime endDate;
		private Integer limitCount;

		private TopOrderedProducts(LocalDateTime startDate, LocalDateTime endDate) {
			this.startDate = startDate;
			this.endDate = endDate;
		}

		public static TopOrderedProducts of(LocalDateTime startDate, LocalDateTime endDate) {
			return new TopOrderedProducts(startDate, endDate);
		}
	}
}
