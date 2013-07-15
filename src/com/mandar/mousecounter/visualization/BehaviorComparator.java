package com.mandar.mousecounter.visualization;

import java.util.Comparator;

public class BehaviorComparator implements Comparator<BehaviorInfo>{

	@Override
	public int compare(BehaviorInfo a, BehaviorInfo b) {
		if(a != null && b != null){
			return a.getBehaviorEnum().ordinal() - b.getBehaviorEnum().ordinal();
		}else {
			return 0;
		}
		
	}

}