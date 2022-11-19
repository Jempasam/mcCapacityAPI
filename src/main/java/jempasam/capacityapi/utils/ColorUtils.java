package jempasam.capacityapi.utils;

import java.util.Arrays;

public class ColorUtils {
	public static int[] asRGB(int i) {
		return new int[] {
			i>>16&0xff,
			i>>8&0xff,
			i&0xff
			};
	}
	
	public static int asInt(int[] rgb) {
		return (rgb[0]<<16)+(rgb[1]<<8)+rgb[2];
	}
	
	public static int rgbOffset(int[] rgb, int r, int g, int b) {
		rgb[0]+=r;
		if(rgb[0]<0)rgb[0]=0;
		else if(rgb[0]>255)rgb[0]=255;
		
		rgb[1]+=g;
		if(rgb[1]<0)rgb[1]=0;
		else if(rgb[1]>255)rgb[1]=255;
		
		rgb[2]+=b;
		if(rgb[2]<0)rgb[2]=0;
		else if(rgb[2]>255)rgb[2]=255;
		return asInt(rgb);
	}
	
	public static int offset(int color, int r, int g, int b) {
		int rgb[]=asRGB(color);
		return rgbOffset(rgb, r, g, b);
	}
	
	public static int lightness(int color, int lightness) {
		return offset(color, lightness, lightness, lightness);
	}
	
	public static int saturation(int color, int saturation) {
		int rgb[]=asRGB(color);
		int middle=(rgb[0]+rgb[1]+rgb[2])/3;
		int r=0;
		if(rgb[0]>middle)r=saturation;
		else if(rgb[0]<middle)r=-saturation;
		int g=0;
		if(rgb[1]>middle)g=saturation;
		else if(rgb[1]<middle)g=-saturation;
		int b=0;
		if(rgb[2]>middle)b=saturation;
		else if(rgb[2]<middle)b=-saturation;
		
		return rgbOffset(rgb, r, g, b);
	}
	
	public static int shiftto(int from, int to, float proportion) {
		if(proportion<0)proportion=0;
		if(proportion>1)proportion=1;
		
		int f[]=asRGB(from);
		int t[]=asRGB(to);
		int ret[]=new int[3];
		
		ret[0]=(int)( t[0]*proportion + f[0]*(1-proportion) );
		ret[1]=(int)( t[1]*proportion + f[1]*(1-proportion) );
		ret[2]=(int)( t[2]*proportion + f[2]*(1-proportion) );
		
		return asInt(ret);
	}
}
