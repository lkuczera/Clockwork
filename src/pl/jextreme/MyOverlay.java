package pl.jextreme;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MyOverlay extends ItemizedOverlay<OverlayItem> {
	private List<OverlayItem> items = new ArrayList<OverlayItem>(); 
	
	public MyOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	@Override
	protected OverlayItem createItem(int i) {
		return items.get(i);
	}

	@Override
	public int size() {
		return items.size();
	}
	
	public void addItem(OverlayItem item) {
		items.add(item);
		populate();
	}
	
	public void clear() {
		items.clear();
	}
}
