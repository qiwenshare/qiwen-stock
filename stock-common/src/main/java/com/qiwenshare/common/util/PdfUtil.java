package com.qiwenshare.common.util;

import com.qiwenshare.common.domain.PdfDomainVO;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDCcitt;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDPixelMap;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.apache.pdfbox.util.PDFOperator;
import org.apache.pdfbox.util.PDFTextStripper;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PdfUtil {
    /***
     * 创建1到多个空白页面
     * @param file
     * @throws IOException
     */
    public static void createBlank( String file ) throws IOException
    {
        //首先创建pdf文档类
        PDDocument document = null;
        try
        {
            document = new PDDocument();
            //实例化pdf页对象
            PDPage blankPage = new PDPage();
            PDPage blankPage1 = new PDPage();
            PDPage blankPage2 = new PDPage();
            //插入文档类
            document.addPage( blankPage );
            document.addPage( blankPage1 );
            document.addPage( blankPage2 );
            //记得一定要写保存路径,如"H:\\text.pdf"
            //document.save( file );
            System.out.println("over");
        }
        finally
        {
            if( document != null )
            {
                document.close();
            }
        }
    }

    /**
     * 读取pdf中文字信息(全部)
     */
    public static void readPdfFile(String fileurl){
        //创建文档对象
        PDDocument doc =null;
        String content="";
        try {
            //加载一个pdf对象
            doc =PDDocument.load(new File(fileurl));
            //获取一个PDFTextStripper文本剥离对象
            PDFTextStripper textStripper =new PDFTextStripper("GBK");
            content=textStripper.getText(doc);
            //vo.setContent(content);
            System.out.println("内容:"+content);
            System.out.println("全部页数"+doc.getNumberOfPages());
            //关闭文档
            doc.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void main(String[] args) {
        readPdfFile("G:\\开发资料\\中国银联无卡快捷支付技术规范\\中国银联无卡快捷支付技术规范（2019）\\中国银联无卡快捷支付技术规范_第1部分_联机交易处理及报文接口规范（2019）.pdf");
        //readPdfFile("G:\\OneDrive\\个人资料\\毕业设计\\自动1303-马超-毕业论文.pdf");
    }

    /**
     * 读取pdf中文字信息(指定从第几页开始)
     */
    public static PdfDomainVO readPageNO(PdfDomainVO vo){
        String content="";
        try{
            PDDocument document = PDDocument.load(vo.getInputfile());
            // 获取页码
            int pages = document.getNumberOfPages();
            // 读文本内容
            PDFTextStripper stripper=new PDFTextStripper();
            // 设置按顺序输出
            stripper.setSortByPosition(true);
            stripper.setStartPage(vo.getPageno());
            stripper.setEndPage(vo.getPageno());
            //获取内容
            content = stripper.getText(document);
            vo.setContent(content);
            System.out.println("function : readPageNO over");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vo;
    }

    /**
     * 替换指定pdf文件的文字内容
     * @param vo
     */
    public static PdfDomainVO replaceContent(PdfDomainVO vo)
            throws IOException, COSVisitorException {
        //创建一个文档对象
        PDDocument doc =null;
        try {
            //加载文件
            doc =PDDocument.load(vo.getInputfile());
            //获取全部页数
            List pages= doc.getDocumentCatalog().getAllPages();
            //获取与i对应的页面
            PDPage page = (PDPage)pages.get( vo.getPageno() );
            //流对象来接收当前page的内容
            PDStream contents = page.getContents();
            //PDF流对象剖析器(这将解析一个PDF字节流并提取操作数,等等)
            PDFStreamParser parser =new PDFStreamParser(contents.getStream());
            //这将分析流中的标记
            parser.parse();
            //用list存流中的所有标记
            List tokens =parser.getTokens();
            for (int j = 0; j < tokens.size(); j++) {
                //创建一个object对象去接收标记
                Object next = tokens.get( j );
                //instanceof判断其左边对象是否为其右边类的实例
                if(next  instanceof PDFOperator) {
                    //pdf操作器对象
                    PDFOperator op =(PDFOperator)next;
                    //TJ和TJ是显示的两个操作符。
                    //PDF中的字符串
                    if(op.getOperation().equals("Tj")){
                        //COSString对象>>创建java字符串的一个新的文本字符串。
                        COSString previous = (COSString)tokens.get( j-1 );
                        //将此字符串的内容作为PDF文本字符串返回。
                        String string=previous.getString();
                        //replaceFirst>>替换第一个字符
                        string = string.replaceFirst( vo.getStrtofind(), vo.getMessage() );
                        System.out.println(string);
                        System.out.println(string.getBytes("GBK"));
                        //重置COSString对象
                        previous.reset();
                        //设置字符编码格式
                        previous.append(string.getBytes("GBK") );
                    }else if(op.getOperation().equals("TJ")){
                        //COSArray是pdfbase对象数组,作为PDF文档的一部分
                        COSArray previous  =(COSArray)tokens.get( j-1 );
                        //循环previous
                        for (int k = 0; k < previous.size(); k++) {
                            //这将从数组中获取一个对象,这将取消引用该对象
                            //如果对象为cosnull，则返回null
                            Object arrElement = previous.getObject( k );
                            if( arrElement instanceof COSString ){
                                //COSString对象>>创建java字符串的一个新的文本字符串。
                                COSString cosString =(COSString)arrElement;
                                //将此字符串的内容作为PDF文本字符串返回。
                                String string =cosString.getString();
                                //替换
                                string = string.replaceFirst(  vo.getStrtofind(), vo.getMessage());
                                //重置COSString对象
                                cosString.reset();
                                //设置字符编码格式
                                cosString.append(string.getBytes("GBK") );
                            }
                        }
                    }
                }
            }
            //创建一个PDStream 流对象
            PDStream updatedStream = new PDStream(doc);
            //创建一个输出流接收updatedStream
            OutputStream out =updatedStream.createOutputStream();
            //将接受一个列表并写出它们的流。
            ContentStreamWriter tokenWriter  =new ContentStreamWriter(out);
            //写入一系列标记，后面跟着一行新行
            tokenWriter.writeTokens(tokens);
            //当前页设置新的内容
            page.setContents( updatedStream );
            //修改后保存的路径
            doc.save(vo.getOutputfile());
            //操作后的页数
            vo.setAfterPages(doc.getNumberOfPages());
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if( doc != null ){
                //关闭文档
                doc.close();
            }
        }
        return vo;
    }


    /**
     * 在pdf中插入图片
     * @throws IOException
     * @throws COSVisitorException
     */
    public static PdfDomainVO  insertImage( PdfDomainVO vo )
            throws IOException, COSVisitorException{
        //偏移量设置
        String[] position =vo.getPosition().split(",");
        int x =Integer.valueOf(position[0]);
        int y =Integer.valueOf(position[position.length-1]);
        //创建一个文档对象
        PDDocument doc =null;
        try {
            //加载
            doc = PDDocument.load(vo.getInputfile());
            //获取加载进来的pdf文件的页面
            PDPage page = (PDPage)doc.getDocumentCatalog().getAllPages().get( vo.getPageno() );
            //pdfbox中图片对象类
            PDXObjectImage ximage = null;
            //判断是否是.jpg格式的图片
            if( vo.getImagefile().toLowerCase().endsWith( ".jpg" ) ){
                //传入一张图片
                ximage = new PDJpeg(doc, new FileInputStream( vo.getImagefile() ) );
            }//如果是tif或tiff格式
            else if (vo.getImagefile().toLowerCase().endsWith(".tif") || vo.getImagefile().toLowerCase().endsWith(".tiff")){
                ximage = new PDCcitt(doc, new RandomAccessFile(new File(vo.getImagefile()),"r"));
            }else{
                //Image和BufferedImage的主要作用就是将一副图片加载到内存中
                BufferedImage awtImage = ImageIO.read( new File( vo.getImagefile() ) );
                ximage = new PDPixelMap(doc, awtImage);
            }
            //这是选择如何处理流：覆盖、追加
            PDPageContentStream contentStream = new PDPageContentStream(doc, page, true, true);
            //控制图片的大小
            float scale = vo.getImgSize();
            scale = scale/10;//(这个值最好是0.1~1,0.5就已经很大了)

            //ximage.setHeight(ximage.getHeight()/5);
            //ximage.setWidth(ximage.getWidth()/5);
            System.out.println(ximage.getHeight());
            System.out.println(ximage.getWidth());
            //设置位移等参数
            contentStream.drawXObject(ximage, x, y, ximage.getWidth()*scale, ximage.getHeight()*scale);
            //关闭流对象
            contentStream.close();
            //保存路径
            doc.save( vo.getOutputfile() );
            //操作后的页数
            vo.setAfterPages(doc.getNumberOfPages());
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if( doc != null ){
                //关闭文档
                doc.close();
            }
        }
        return vo;
    }

    /***
     * 指定页数的PDF文件转换为图片：
     */
    public static PdfDomainVO toImage( PdfDomainVO vo ) {
        try {
            //加载
            PDDocument doc = PDDocument.load(vo.getInputfile());
            //
            //int pageCount = doc.getPageCount();
            ////获取全部页数
            //指定单页转pdf
            List pages = doc.getDocumentCatalog().getAllPages();
            if(vo.getPageno()!=null){
                String count=(int)(Math.random()*1000)+"-"+(int)(Math.random()*1000);
                //接收页面
                PDPage page = (PDPage) pages.get(vo.getPageno());
                //定义图片操作对象来设置图片
                BufferedImage image = page.convertToImage();
                //定义迭代器对象存储
                Iterator iter = ImageIO.getImageWritersBySuffix("jpg");
                //图片写入器对象写入图片
                ImageWriter writer = (ImageWriter) iter.next();
                //循环保存图片
                File outFile = new File(vo.getOutputfile()+vo.getFilename()+"-"+(vo.getPageno()+1)+".jpg");
                //创建文件输出流对象
                FileOutputStream out = new FileOutputStream(outFile);
                //ImageIO去实现ImageOutputStream获取当前图片
                ImageOutputStream outImage = ImageIO.createImageOutputStream(out);
                writer.setOutput(outImage);
                writer.write(new IIOImage(image, null, null));
            }else{
                //循环
                for (int i = 0; i < pages.size(); i++) {
                    //接收页面
                    PDPage page = (PDPage) pages.get(i);
                    //定义图片操作对象来设置图片
                    BufferedImage image = page.convertToImage();
                    //定义迭代器对象存储
                    Iterator iter = ImageIO.getImageWritersBySuffix("jpg");
                    //图片写入器对象写入图片
                    ImageWriter writer = (ImageWriter) iter.next();
                    //循环保存图片
                    File outFile = new File(vo.getOutputfile()+i+".jpg");
                    //创建文件输出流对象
                    FileOutputStream out = new FileOutputStream(outFile);
                    //ImageIO去实现ImageOutputStream获取当前图片
                    ImageOutputStream outImage = ImageIO.createImageOutputStream(out);
                    writer.setOutput(outImage);
                    writer.write(new IIOImage(image, null, null));
                }
            }
            //关文档
            doc.close();
            //操作后的页数
            vo.setAfterPages(doc.getNumberOfPages());
            System.out.println("over");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vo;
    }


    /***
     * 指定页插入一段文字
     * @param vo
     * @throws IOException
     * @throws COSVisitorException
     */
    public static PdfDomainVO InsertPageContent (PdfDomainVO vo ) throws IOException, COSVisitorException
    {
        // the document
        PDDocument doc = null;
        try
        {
            doc = PDDocument.load( vo.getInputfile() );
            List allPages = doc.getDocumentCatalog().getAllPages();
            PDFont font = PDType1Font.HELVETICA_BOLD;
            //字体大小
            float fontSize = 36.0f;
            PDPage page = (PDPage)allPages.get( vo.getPageno() );
            PDRectangle pageSize = page.findMediaBox();
            float stringWidth = font.getStringWidth( vo.getMessage() )*fontSize/1000f;
            // calculate to center of the page
            int rotation = page.findRotation();
            boolean rotate = rotation == 90 || rotation == 270;
            float pageWidth = rotate ? pageSize.getHeight() : pageSize.getWidth();
            float pageHeight = rotate ? pageSize.getWidth() : pageSize.getHeight();
            double centeredXPosition = rotate ? pageHeight/2f : (pageWidth - stringWidth)/2f;
            double centeredYPosition = rotate ? (pageWidth - stringWidth)/2f : pageHeight/2f;
            // append the content to the existing stream
            PDPageContentStream contentStream = new PDPageContentStream(doc, page, true, true,true);
            contentStream.beginText();
            // set font and font size
            contentStream.setFont( font, fontSize );
            // set text color to red
            contentStream.setNonStrokingColor(255, 0, 0);
            if (rotate)
            {
                // rotate the text according to the page rotation
                contentStream.setTextRotation(Math.PI/2, centeredXPosition, centeredYPosition);
            }
            else
            {
                contentStream.setTextTranslation(centeredXPosition, centeredYPosition);
            }
            contentStream.drawString( vo.getMessage() );
            contentStream.endText();
            contentStream.close();
            vo.setAfterPages(doc.getNumberOfPages());
            doc.save( vo.getOutputfile() );
            System.out.println("over");
        }
        finally
        {
            if( doc != null )
            {
                doc.close();
            }
        }
        return vo;
    }

    /**
     * 提取图片并保存
     * @param pdfDomainVO
     * @throws IOException
     *
     */
    public static PdfDomainVO extractImage(PdfDomainVO vo ) throws IOException{
        //创建文档
        PDDocument doc=null;
        try{
            //加载 pdf 文档,获取PDDocument文档对象
            doc=PDDocument.load(vo.getInputfile());
            /** 文档页面信息 **/
            //获取PDDocumentCatalog文档目录对象
            PDDocumentCatalog catalog = doc.getDocumentCatalog();
            //获取文档页面PDPage列表
            List pages = catalog.getAllPages();
            int pageNum=pages.size();   //文档页数
            PDPage page = null;
            if(vo.getPageno()!=null){
                page = ( PDPage ) pages.get( vo.getPageno() );
                if( null != page ){
                    PDResources resource = page.findResources();
                    //获取页面图片信息
                    Map<String, PDXObjectImage> imgs = resource.getImages();
                    for(Map.Entry<String,PDXObjectImage> me: imgs.entrySet()){
                        //System.out.println(me.getKey());
                        PDXObjectImage img = me.getValue();
                        //保存图片，会自动添加图片后缀类型
                        img.write2file( vo.getOutputfile() + vo.getFilename()+"-"+(vo.getPageno()+1) );
                    }
                }
            }else{
                //遍历每一页
                for( int i = 0; i < pageNum; i++ ){
                    //取得第i页
                    page = ( PDPage ) pages.get( i );
                    if( null != page ){
                        PDResources resource = page.findResources();
                        //获取页面图片信息
                        Map<String,PDXObjectImage> imgs = resource.getImages();
                        for(Map.Entry<String,PDXObjectImage> me: imgs.entrySet()){
                            String count=(int)(Math.random()*1000)+"-"+(int)(Math.random()*1000);
                            //System.out.println(me.getKey());
                            PDXObjectImage img = me.getValue();
                            //保存图片，会自动添加图片后缀类型
                            img.write2file( vo.getOutputfile() + count );
                        }
                    }
                }
            }
            //操作后的页数
            vo.setAfterPages(doc.getNumberOfPages());
            System.out.println("extractImage:over");
        }  finally
        {
            if( doc != null )
            {
                doc.close();
            }
        }
        return vo;
    }

//    /***
//     * PDF文档中删除页面
//     * 一个PDF文档必须至少有一页，且不能删除最后一页！
//     * @param inputFile
//     * @param outputFile
//     * @throws Exception
//     */
//    public static PdfDomainVO removePage(PdfDomainVO vo) throws Exception
//    {
//        vo.setStatus(Details.FailStatus);
//        PDDocument document = null;
//        try
//        {
//            document = PDDocument.load(vo.getInputfile() );
//            if( document.isEncrypted() )
//            {
//                throw new IOException( "Encrypted documents are not supported for this example" );
//            }
//            if( document.getNumberOfPages() <= 1 )
//            {
//                throw new IOException( "Error: A PDF document must have at least one page, " +
//                        "cannot remove the last page!");
//            }
//            document.removePage( vo.getPageno() );
//            document.save(vo.getOutputfile() );
//            //操作后的页数
//            vo.setAfterPages(document.getNumberOfPages());
//            //设置成功状态
//            vo.setStatus(Details.SuccessStatus);
//            System.out.println("over");
//        }
//        finally
//        {
//            if( document != null )
//            {
//                document.close();
//            }
//        }
//        return vo;
//    }
}
