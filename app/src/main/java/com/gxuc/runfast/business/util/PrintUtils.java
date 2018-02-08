package com.gxuc.runfast.business.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.gxuc.runfast.business.data.bean.Order;
import com.gxuc.runfast.business.data.bean.OrderGoods;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

public class PrintUtils {
    /**
     * 打印纸一行最大的字节
     */
    private static final int LINE_BYTE_SIZE = 32;

    private static final int LEFT_LENGTH = 24;

    private static final int RIGHT_LENGTH = 8;

    /**
     * 左侧汉字最多显示几个文字
     */
//    private static final int LEFT_TEXT_MAX_LENGTH = 8;
    private static final int LEFT_TEXT_MAX_LENGTH = 10;

    /**
     * 小票打印菜品的名称，上限调到8个字
     */
    public static final int MEAL_NAME_MAX_LENGTH = 8;


    private static OutputStream outputStream = null;

    public static OutputStream getOutputStream() {
        return outputStream;
    }

    public static void setOutputStream(OutputStream outputStream) {
        PrintUtils.outputStream = outputStream;
    }


    /**
     * 打印文字
     *
     * @param text 要打印的文字
     */
    public static void printText(String text) {
        try {
            byte[] data = text.getBytes("gbk");
            outputStream.write(data, 0, data.length);
            outputStream.flush();
        } catch (IOException e) {
            //Toast.makeText(this.context, "发送失败！", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 设置打印格式
     *
     * @param command 格式指令
     */
    public static void selectCommand(byte[] command) {
        try {
            outputStream.write(command);
            outputStream.flush();
        } catch (IOException e) {
            //Toast.makeText(this.context, "发送失败！", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 复位打印机
     */
    public static final byte[] RESET = {0x1b, 0x40};

    /**
     * 左对齐
     */
    public static final byte[] ALIGN_LEFT = {0x1b, 0x61, 0x00};

    /**
     * 中间对齐
     */
    public static final byte[] ALIGN_CENTER = {0x1b, 0x61, 0x01};

    /**
     * 右对齐
     */
    public static final byte[] ALIGN_RIGHT = {0x1b, 0x61, 0x02};

    /**
     * 选择加粗模式
     */
    public static final byte[] BOLD = {0x1b, 0x45, 0x01};

    /**
     * 取消加粗模式
     */
    public static final byte[] BOLD_CANCEL = {0x1b, 0x45, 0x00};

    /**
     * 宽高加倍
     */
    public static final byte[] DOUBLE_HEIGHT_WIDTH = {0x1d, 0x21, 0x11};

    /**
     * 宽加倍
     */
    public static final byte[] DOUBLE_WIDTH = {0x1d, 0x21, 0x10};

    /**
     * 高加倍
     */
    public static final byte[] DOUBLE_HEIGHT = {0x1d, 0x21, 0x01};

    /**
     * 字体不放大
     */
    public static final byte[] NORMAL = {0x1d, 0x21, 0x00};

    /**
     * 设置默认行间距
     */
    public static final byte[] LINE_SPACING_DEFAULT = {0x1b, 0x32};

    /**
     * 设置行间距
     */
//	public static final byte[] LINE_SPACING = {0x1b, 0x32};//{0x1b, 0x33, 0x14};  // 20的行间距（0，255）


//	final byte[][] byteCommands = {
//			{ 0x1b, 0x61, 0x00 }, // 左对齐
//			{ 0x1b, 0x61, 0x01 }, // 中间对齐
//			{ 0x1b, 0x61, 0x02 }, // 右对齐
//			{ 0x1b, 0x40 },// 复位打印机
//			{ 0x1b, 0x4d, 0x00 },// 标准ASCII字体
//			{ 0x1b, 0x4d, 0x01 },// 压缩ASCII字体
//			{ 0x1d, 0x21, 0x00 },// 字体不放大
//			{ 0x1d, 0x21, 0x11 },// 宽高加倍
//			{ 0x1b, 0x45, 0x00 },// 取消加粗模式
//			{ 0x1b, 0x45, 0x01 },// 选择加粗模式
//			{ 0x1b, 0x7b, 0x00 },// 取消倒置打印
//			{ 0x1b, 0x7b, 0x01 },// 选择倒置打印
//			{ 0x1d, 0x42, 0x00 },// 取消黑白反显
//			{ 0x1d, 0x42, 0x01 },// 选择黑白反显
//			{ 0x1b, 0x56, 0x00 },// 取消顺时针旋转90°
//			{ 0x1b, 0x56, 0x01 },// 选择顺时针旋转90°
//	};

    /**
     * 打印两列
     *
     * @param leftText  左侧文字
     * @param rightText 右侧文字
     * @return
     */
    @SuppressLint("NewApi")
    public static String printTwoData(String leftText, String rightText) {
        StringBuilder sb = new StringBuilder();
        int leftTextLength = getBytesLength(leftText);
        int rightTextLength = getBytesLength(rightText);
        sb.append(leftText);

        // 计算两侧文字中间的空格
        int marginBetweenMiddleAndRight = LINE_BYTE_SIZE - leftTextLength - rightTextLength;

        for (int i = 0; i < marginBetweenMiddleAndRight; i++) {
            sb.append(" ");
        }
        sb.append(rightText);
        return sb.toString();
    }

    /**
     * 打印三列
     *
     * @param leftText   左侧文字
     * @param middleText 中间文字
     * @param rightText  右侧文字
     */
    public static String printThreeData(String leftText, String middleText, String rightText) {
        StringBuilder sb = new StringBuilder();
        // 左边最多显示 LEFT_TEXT_MAX_LENGTH 个汉字 + 两个点
        String leftText1 = "";
        if (leftText.length() > LEFT_TEXT_MAX_LENGTH) {
            leftText1 = leftText.substring(LEFT_TEXT_MAX_LENGTH, leftText.length() - 1);
            leftText = leftText.substring(0, LEFT_TEXT_MAX_LENGTH);
        }

        int leftTextLength = getBytesLength(leftText);
        int middleTextLength = getBytesLength(middleText);
        int rightTextLength = getBytesLength(rightText);

        sb.append(leftText);
        // 计算左侧文字和中间文字的空格长度
        int marginBetweenLeftAndMiddle = LEFT_LENGTH - leftTextLength - middleTextLength / 2;

        for (int i = 0; i < marginBetweenLeftAndMiddle; i++) {
            sb.append(" ");
        }
        sb.append(middleText);

        // 计算右侧文字和中间文字的空格长度
        int marginBetweenMiddleAndRight = RIGHT_LENGTH - middleTextLength / 2 - rightTextLength;

        for (int i = 0; i < marginBetweenMiddleAndRight; i++) {
            sb.append(" ");
        }

        // 打印的时候发现，最右边的文字总是偏右一个字符，所以需要删除一个空格
        sb.delete(sb.length() - 1, sb.length()).append(rightText);
        if (!"".equals(leftText1)) {
            sb.append(printThreeData(leftText1, " ", " \n"));
        }
        return sb.toString();
    }

    /**
     * 获取数据长度
     */
    @SuppressLint("NewApi")
    private static int getBytesLength(String msg) {
        return msg.getBytes(Charset.forName("GB2312")).length;
    }

    /**
     * 格式化菜品名称，最多显示MEAL_NAME_MAX_LENGTH个数
     */
    public static String formatMealName(String name) {
        if (TextUtils.isEmpty(name)) {
            return name;
        }
        if (name.length() > MEAL_NAME_MAX_LENGTH) {
            return name.substring(0, 8) + "..";
        }
        return name;
    }

    public static void printTest(Order order) {
        PrintUtils.selectCommand(PrintUtils.RESET);
        PrintUtils.selectCommand(PrintUtils.LINE_SPACING_DEFAULT);
        PrintUtils.selectCommand(PrintUtils.ALIGN_CENTER);
        PrintUtils.selectCommand(PrintUtils.DOUBLE_HEIGHT_WIDTH);
        PrintUtils.printText(PrintUtils.printTwoData("订单编号", order.orderNo + "\n"));
        PrintUtils.printText("\n\n");
    }

    public static void print(Order order) {
        PrintUtils.selectCommand(PrintUtils.RESET);
        PrintUtils.selectCommand(PrintUtils.LINE_SPACING_DEFAULT);
        PrintUtils.selectCommand(PrintUtils.ALIGN_CENTER);
        PrintUtils.selectCommand(PrintUtils.DOUBLE_HEIGHT_WIDTH);
        PrintUtils.printText("#" + order.orderNumber + " 跑腿快车\n\n");

        PrintUtils.selectCommand(PrintUtils.NORMAL);

        PrintUtils.printText(order.businessName);
        PrintUtils.printText("\n\n");

        PrintUtils.selectCommand(PrintUtils.ALIGN_LEFT);

        PrintUtils.printText(PrintUtils.printTwoData("订单编号", order.orderNo + "\n"));
        PrintUtils.printText(PrintUtils.printTwoData("付款时间", order.payTime + "\n"));
//        if (!TextUtils.isEmpty(order.courier)) {
//            PrintUtils.printText(PrintUtils.printTwoData("送货员", order.courier + "：" + order.courierPhone + "\n"));
//        }
        PrintUtils.selectCommand(PrintUtils.ALIGN_LEFT);

        if (!TextUtils.isEmpty(order.remark)) {
            PrintUtils.selectCommand(PrintUtils.LINE_SPACING_DEFAULT);
            PrintUtils.selectCommand(PrintUtils.DOUBLE_HEIGHT_WIDTH);
            PrintUtils.printText("备注：" + order.remark + "\n");
        }
        PrintUtils.selectCommand(PrintUtils.NORMAL);
        PrintUtils.selectCommand(PrintUtils.ALIGN_LEFT);

//        PrintUtils.printText("\n--------------------------------\n");
        List<OrderGoods> goods = order.goods;
        PrintUtils.selectCommand(PrintUtils.BOLD);
//        PrintUtils.printText(PrintUtils.printThreeData("商品名称", "数量", "金额\n"));
        PrintUtils.printText("\n--------------商品--------------\n\n");
        PrintUtils.selectCommand(PrintUtils.BOLD_CANCEL);

        PrintUtils.selectCommand(PrintUtils.LINE_SPACING_DEFAULT);
        PrintUtils.selectCommand(PrintUtils.DOUBLE_HEIGHT);
        if (goods != null) {
            for (OrderGoods g : goods) {
                PrintUtils.printText(PrintUtils.printThreeData(g.name + g.standard + g.option + " ", "x" + String.valueOf(g.count), g.price + "\n"));
                if (!TextUtils.isEmpty(g.remark)) {
                    PrintUtils.printText("其他要求:" + g.remark + "\n");
                }
                if (g.activityType == 3 && !TextUtils.isEmpty(g.goodsAct)){
                    PrintUtils.printText("赠品: " + g.goodsAct + "\n");
                }
            }
        }
        PrintUtils.selectCommand(PrintUtils.NORMAL);
        PrintUtils.selectCommand(PrintUtils.ALIGN_LEFT);

        PrintUtils.printText("--------------------------------\n");
//        PrintUtils.printText(PrintUtils.printTwoData("合计数量", String.valueOf(order.goodsCount) + "\n"));
        PrintUtils.printText(PrintUtils.printTwoData("配送费", order.deliveryCost + "\n"));
        PrintUtils.printText(PrintUtils.printTwoData("餐盒费", order.packingCharge + "\n"));
        PrintUtils.printText(PrintUtils.printTwoData("优惠券费用", "-" + order.discounts + "\n"));
        PrintUtils.printText(PrintUtils.printTwoData("活动优惠费用", "-" + order.cost + "\n"));
        PrintUtils.printText("********************************\n");
        PrintUtils.selectCommand(PrintUtils.DOUBLE_HEIGHT_WIDTH);
        PrintUtils.selectCommand(PrintUtils.ALIGN_RIGHT);
        PrintUtils.printText("已付: " + order.payAmount + "\n");
//        PrintUtils.printText(PrintUtils.printTwoData("已付", order.payAmount + "\n"));
        PrintUtils.selectCommand(PrintUtils.NORMAL);
        PrintUtils.selectCommand(PrintUtils.ALIGN_LEFT);
        PrintUtils.printText("--------------------------------\n");
        PrintUtils.selectCommand(PrintUtils.BOLD);
        PrintUtils.selectCommand(PrintUtils.LINE_SPACING_DEFAULT);
        PrintUtils.selectCommand(PrintUtils.DOUBLE_HEIGHT_WIDTH);
        PrintUtils.printText(order.shopperAddress + "\n");
        PrintUtils.printText("\n");
        PrintUtils.printText(order.shopperName + "\n");
        PrintUtils.printText("\n");
        PrintUtils.printText(order.shopperPhone + "\n");
        PrintUtils.printText("\n");
        PrintUtils.printText("--------------------------------\n");

        PrintUtils.printText("\n\n\n\n\n");
    }
}
