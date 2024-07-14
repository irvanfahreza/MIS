package id.go.bpjskesehatan.service.v2;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.lang3.text.WordUtils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import id.go.bpjskesehatan.database.Koneksi;
import id.go.bpjskesehatan.entitas.Metadata;
import id.go.bpjskesehatan.entitas.Result;
import id.go.bpjskesehatan.entitas.organisasi.Office;
import id.go.bpjskesehatan.service.v2.cuti.entitas.Spm;
import id.go.bpjskesehatan.service.v2.cuti.entitas.SpmTemplate;
import id.go.bpjskesehatan.service.v2.entitas.Akun;
import id.go.bpjskesehatan.service.v2.entitas.Anggaran;
import id.go.bpjskesehatan.service.v2.entitas.Program;
import id.go.bpjskesehatan.util.SharedMethod;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Path("v2/pdf")
public class PDFRest {	
	
	@Context
    private ServletContext context;
	
	private String terbilang(Long nilai) {
		String[] bilangan={"","satu","dua","tiga","empat","lima","enam","tujuh","delapan","sembilan","sepuluh","sebelas"};
		
		if(nilai < 12)
            return bilangan[nilai.intValue()];
        if(nilai >=12 && nilai <= 19)
           return bilangan[nilai.intValue() % 10] + " belas";
        if(nilai >= 20 && nilai <= 99)
           return terbilang(nilai / 10) + " puluh " + bilangan[nilai.intValue() % 10];
        if(nilai >= 100 && nilai <= 199)
           return "seratus " + terbilang(nilai % 100);
        if(nilai >= 200 && nilai <= 999)
           return terbilang(nilai / 100) + " ratus " + terbilang(nilai % 100);
        if(nilai >= 1000 && nilai <= 1999)
           return "seribu " + terbilang(nilai % 1000);
        if(nilai >= 2000 && nilai <= 999999)
           return terbilang(nilai / 1000) + " ribu " + terbilang(nilai % 1000);
        if(nilai >= 1000000 && nilai <= 999999999)
           return terbilang(nilai / 1000000) + " juta " + terbilang(nilai % 1000000);
        if(nilai >= 1000000000 && nilai <= 999999999999L)
           return terbilang(nilai / 1000000000) + " milyar " + terbilang(nilai % 1000000000);
        if(nilai >= 1000000000000L && nilai <= 999999999999999L)
           return terbilang(nilai / 1000000000000L) + " triliun " + terbilang(nilai % 1000000000000L);
        if(nilai >= 1000000000000000L && nilai <= 999999999999999999L)
          return terbilang(nilai / 1000000000000000L) + " quadrilyun " + terbilang(nilai % 1000000000000000L);
        return "";
	}
	
	private String kursInfo(double harga) {
		//double harga = 250000000;
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(harga);
	}
	
	private String currency(double harga) {
		DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(harga);
	}
	
	private String kapital(String kalimat) {
		String output = kalimat.substring(0, 1).toUpperCase() + kalimat.substring(1);
		return output;
		//return WordUtils.capitalize(kalimat,new char[]{});
	}
	
	private String kapitalFul(String kalimat) {
		return WordUtils.capitalizeFully(kalimat);
	}
	
	@GET
	@Path("/spm/{token}/{kode}")
	@Produces("application/pdf")
	public Response cetakSPM(@Context HttpHeaders headers, @PathParam("token") String token, @PathParam("kode") Integer kode) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		
		if (SharedMethod.VerifyToken2(token, metadata)) {
			ResponseBuilder response;
			Connection con = null;
			ResultSet rs = null;
			PreparedStatement ps = null;
			String query = null;
			
			SpmTemplate spmTemplate = null;
			Spm spm = null;
			
			try {
				query = "select b.* "
						+ "from cuti.spm a "
						+ "inner join cuti.spmtemplate b on a.kodespmtemplate=b.kode "
						+ "where a.kode=?";
				con = new Koneksi().getConnection();
				ps = con.prepareStatement(query);
				ps.setInt(1, kode);
				rs = ps.executeQuery();
				if(rs.next()) {
					spmTemplate = new SpmTemplate();
					spmTemplate.setKepada(rs.getString("kepada"));
					spmTemplate.setDari(rs.getString("dari"));
					spmTemplate.setDari_deputi(rs.getString("dari_deputi"));
					spmTemplate.setTempat(rs.getString("tempat"));
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
					}
				}
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
					}
				}
			}
			
			try {
				query = "select a.*, aa.nama as namatipe, b.NMPROG  as namaprogram, c.NMAKUN as namaakun, day(a.created_time) as hari, month(a.created_time) as bulan, year(a.created_time) as tahun "
						+ "from cuti.spm a "
						+ "inner join cuti.tipe aa on a.kodetipe=aa.kode "
						+ "left join referensi.vw_kodeprogram b on a.kodeprogram=b.KDPROG "
						+ "left join referensi.vw_kodeakun c on a.kodeakun=c.KDAKUN "
						+ "where a.kode=?";
				con = new Koneksi().getConnection();
				ps = con.prepareStatement(query);
				ps.setInt(1, kode);
				rs = ps.executeQuery();
				if(rs.next()) {
					spmTemplate.setTanggal(rs.getInt("hari"));
					spmTemplate.setBulan(rs.getInt("bulan"));
					spmTemplate.setTahun(rs.getInt("tahun"));
					
					spm = new Spm();
					spm.setKode(rs.getInt("kode"));
					spm.setNoarsip(rs.getString("noarsip"));
					spm.setTotal(rs.getBigDecimal("total"));
					spm.setKepada(rs.getString("kepada"));
					spm.setNamatipe(rs.getString("namatipe"));
					spm.setKodeoffice(rs.getString("kodeoffice"));
					
					Anggaran anggaran = new Anggaran();
					anggaran.setTahun(rs.getInt("tahunanggaran"));
					anggaran.setAlokasi(rs.getBigDecimal("alokasianggaran"));
					anggaran.setRealisasi(rs.getBigDecimal("realisasianggaran"));
					anggaran.setSaldo(rs.getBigDecimal("saldoanggaran"));
					spm.setAnggaran(anggaran);
					
					Program program = new Program();
					program.setKode(rs.getString("kodeprogram"));
					program.setNama(rs.getString("namaprogram"));
					anggaran.setProgram(program);
					
					Akun akun = new Akun();
					akun.setKode(rs.getString("kodeakun"));
					akun.setNama(rs.getString("namaakun"));
					anggaran.setAkun(akun);
					spm.setAnggaran(anggaran);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
					}
				}
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
					}
				}
			}
			
			String[] bulan={"","Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","Nopember","Desember"};
			
			Document document = new Document(PageSize.A4, 70, 70, 120, 80);
			try {
				String path = "/tmp/";
				String filename = "SPM";
				PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path + filename));
			    document.open();
			    
			    Font fontstyle = FontFactory.getFont("Times Roman", 10, BaseColor.BLACK);
			    
			    PdfPTable table = new PdfPTable(3);
			    table.setWidthPercentage(100);
			    table.setTotalWidth(new float[] { 15, 50, 35 });
		        PdfPCell cell1 = new PdfPCell(new Paragraph("Nomor", fontstyle));
		        cell1.setBorder(PdfPCell.NO_BORDER);
		        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell1);
		        PdfPCell cell2 = new PdfPCell(new Paragraph(": " + spm.getNoarsip(), fontstyle));
		        cell2.setBorder(PdfPCell.NO_BORDER);
		        cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell2);
		        PdfPCell cell3 = new PdfPCell(new Paragraph(spmTemplate.getTempat()+", "+spmTemplate.getTanggal()+" "+bulan[spmTemplate.getBulan()]+" "+spmTemplate.getTahun(), fontstyle));
		        cell3.setBorder(PdfPCell.NO_BORDER);
		        cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        table.addCell(cell3);
		        
		        PdfPCell cell4 = new PdfPCell(new Paragraph("Lampiran", fontstyle));
		        cell4.setBorder(PdfPCell.NO_BORDER);
		        cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell4);
		        PdfPCell cell5 = new PdfPCell(new Paragraph(": Satu berkas", fontstyle));
		        cell5.setBorder(PdfPCell.NO_BORDER);
		        cell5.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell5);
		        PdfPCell cell6 = new PdfPCell();
		        cell6.setBorder(PdfPCell.NO_BORDER);
		        cell6.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell6);
		        
		        PdfPCell cell7 = new PdfPCell(new Paragraph("Hal", fontstyle));
		        cell7.setBorder(PdfPCell.NO_BORDER);
		        cell7.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell7);
		        PdfPCell cell8 = new PdfPCell(new Paragraph(": Surat Perintah Membayar", fontstyle));
		        cell8.setBorder(PdfPCell.NO_BORDER);
		        cell8.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell8);
		        PdfPCell cell9 = new PdfPCell();
		        cell9.setBorder(PdfPCell.NO_BORDER);
		        cell9.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell9);
		        
		        document.add(table);
		        
		        Paragraph kosong1 = new Paragraph(" ", fontstyle);
		        document.add(kosong1);
		        
		        
		        Paragraph baris1 = new Paragraph("Yth. "+spmTemplate.getKepada(), fontstyle);
		        Paragraph baris2 = new Paragraph("BPJS Kesehatan", fontstyle);
		        Paragraph baris3 = new Paragraph("di", fontstyle);
		        Paragraph baris4 = new Paragraph(spmTemplate.getTempat(), fontstyle);
		        document.add(baris1);
		        document.add(baris2);
		        document.add(baris3);
		        document.add(baris4);
		        
		        Paragraph kosong2 = new Paragraph(" ", fontstyle);
		        document.add(kosong2);
		        
		        Paragraph par1 = new Paragraph("Saya yang bertanda tangan dibawah ini selaku "+spmTemplate.getDari()+" memerintahkan kepada "+spmTemplate.getKepada()+" untuk melakukan pembayaran :", fontstyle);
		        par1.setAlignment(Element.ALIGN_JUSTIFIED);
		        par1.setFirstLineIndent(30);
		        document.add(par1);
		        
		        Paragraph kosong3 = new Paragraph(" ", fontstyle);
		        document.add(kosong3);
		        
		        PdfPTable table2 = new PdfPTable(3);
		        table2.setWidthPercentage(100);
		        table2.setTotalWidth(new float[] { 20, 2, 68 });
		        PdfPCell cell21 = new PdfPCell(new Paragraph("Sejumlah", fontstyle));
		        cell21.setBorder(PdfPCell.NO_BORDER);
		        cell21.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell21);
		        PdfPCell cell22 = new PdfPCell(new Paragraph(":", fontstyle));
		        cell22.setBorder(PdfPCell.NO_BORDER);
		        cell22.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell22);
		        PdfPCell cell23 = new PdfPCell(new Paragraph(kursInfo(spm.getTotal().doubleValue()), fontstyle));
		        cell23.setBorder(PdfPCell.NO_BORDER);
		        cell23.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell23);
		        
		        PdfPCell cell24 = new PdfPCell(new Paragraph("Dengan huruf", fontstyle));
		        cell24.setBorder(PdfPCell.NO_BORDER);
		        cell24.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell24);
		        PdfPCell cell25 = new PdfPCell(new Paragraph(":", fontstyle));
		        cell25.setBorder(PdfPCell.NO_BORDER);
		        cell25.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell25);
		        PdfPCell cell26 = new PdfPCell(new Paragraph(kapital(terbilang(spm.getTotal().longValue())+" rupiah"), fontstyle));
		        cell26.setBorder(PdfPCell.NO_BORDER);
		        cell26.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		        table2.addCell(cell26);
		        
		        PdfPCell cell27 = new PdfPCell(new Paragraph("Kepada", fontstyle));
		        cell27.setBorder(PdfPCell.NO_BORDER);
		        cell27.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell27);
		        PdfPCell cell28 = new PdfPCell(new Paragraph(":", fontstyle));
		        cell28.setBorder(PdfPCell.NO_BORDER);
		        cell28.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell28);
		        PdfPCell cell29 = new PdfPCell(new Paragraph(kapitalFul(spm.getKepada()), fontstyle));
		        cell29.setBorder(PdfPCell.NO_BORDER);
		        cell29.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell29);
		        
		        PdfPCell cell210 = new PdfPCell(new Paragraph("Untuk pembayaran", fontstyle));
		        cell210.setBorder(PdfPCell.NO_BORDER);
		        cell210.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell210);
		        PdfPCell cell211 = new PdfPCell(new Paragraph(":", fontstyle));
		        cell211.setBorder(PdfPCell.NO_BORDER);
		        cell211.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell211);
		        PdfPCell cell212 = new PdfPCell(new Paragraph("Tunjangan "+spm.getNamatipe(), fontstyle));
		        cell212.setBorder(PdfPCell.NO_BORDER);
		        cell212.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell212);
		        
		        document.add(table2);
		        
		        Paragraph kosong4 = new Paragraph(" ", fontstyle);
		        document.add(kosong4);
		        
		        Paragraph atasdasar = new Paragraph("Atas dasar :", fontstyle);
		        document.add(atasdasar);
		        Paragraph atasdasar1 = new Paragraph("1. Kertas Kerja Perhitungan", fontstyle);
		        atasdasar1.setIndentationLeft(20);
		        document.add(atasdasar1);
		        Paragraph atasdasar2 = new Paragraph("2. Bukti Lainnya (Terlampir)", fontstyle);
		        atasdasar2.setIndentationLeft(20);
		        document.add(atasdasar2);
		        
		        Paragraph kosong5 = new Paragraph(" ", fontstyle);
		        document.add(kosong5);
		        
		        Paragraph beban = new Paragraph("Dibebankan pada :", fontstyle);
		        document.add(beban);
		        
		        Paragraph kosong6 = new Paragraph(" ", fontstyle);
		        document.add(kosong6);
		        
		        PdfPTable tbl = new PdfPTable(7);
		        tbl.setWidthPercentage(100);
		        tbl.setTotalWidth(new float[] { 5, 26, 12, 12, 15, 15, 15 });
		        PdfPCell c1 = new PdfPCell(new Paragraph("No", fontstyle));
		        c1.setBorder(Rectangle.BOX);
		        c1.setRowspan(2);
		        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(c1);
		        PdfPCell c2 = new PdfPCell(new Paragraph("Mata Anggaran", fontstyle));
		        c2.setBorder(Rectangle.BOX);
		        c2.setRowspan(2);
		        c2.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(c2);
		        PdfPCell c3 = new PdfPCell(new Paragraph("Kode", fontstyle));
		        c3.setBorder(Rectangle.BOX);
		        c3.setColspan(2);
		        c3.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(c3);
		        PdfPCell c4 = new PdfPCell(new Paragraph("Alokasi Anggaran Tahun "+spm.getAnggaran().getTahun(), fontstyle));
		        c4.setBorder(Rectangle.BOX);
		        c4.setRowspan(2);
		        c4.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(c4);
		        PdfPCell c5 = new PdfPCell(new Paragraph("Realisasi Anggaran", fontstyle));
		        c5.setBorder(Rectangle.BOX);
		        c5.setRowspan(2);
		        c5.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c5.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(c5);
		        PdfPCell c6 = new PdfPCell(new Paragraph("Saldo Anggaran", fontstyle));
		        c6.setBorder(Rectangle.BOX);
		        c6.setRowspan(2);
		        c6.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c6.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(c6);
		        PdfPCell c7 = new PdfPCell(new Paragraph("Program", fontstyle));
		        c7.setBorder(Rectangle.BOX);
		        c7.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c7.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(c7);
		        PdfPCell c8 = new PdfPCell(new Paragraph("Akun", fontstyle));
		        c8.setBorder(Rectangle.BOX);
		        c8.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c8.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(c8);
		        //-----------------------------------
		        PdfPCell isi1 = new PdfPCell(new Paragraph("1", fontstyle));
		        isi1.setBorder(Rectangle.BOX);
		        isi1.setHorizontalAlignment(Element.ALIGN_CENTER);
		        isi1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(isi1);
		        PdfPCell isi2 = new PdfPCell(new Paragraph(kapitalFul(spm.getAnggaran().getProgram().getNama())+" - "+kapitalFul(spm.getAnggaran().getAkun().getNama()), fontstyle));
		        isi2.setBorder(Rectangle.BOX);
		        isi2.setHorizontalAlignment(Element.ALIGN_CENTER);
		        isi2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(isi2);
		        PdfPCell isi3 = new PdfPCell(new Paragraph(spm.getAnggaran().getProgram().getKode(), fontstyle));
		        isi3.setBorder(Rectangle.BOX);
		        isi3.setHorizontalAlignment(Element.ALIGN_CENTER);
		        isi3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(isi3);
		        PdfPCell isi4 = new PdfPCell(new Paragraph(spm.getAnggaran().getAkun().getKode(), fontstyle));
		        isi4.setBorder(Rectangle.BOX);
		        isi4.setHorizontalAlignment(Element.ALIGN_CENTER);
		        isi4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(isi4);
		        PdfPCell isi5 = new PdfPCell(new Paragraph(currency(spm.getAnggaran().getAlokasi().doubleValue()), fontstyle));
		        isi5.setBorder(Rectangle.BOX);
		        isi5.setHorizontalAlignment(Element.ALIGN_CENTER);
		        isi5.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(isi5);
		        PdfPCell isi6 = new PdfPCell(new Paragraph(currency(spm.getAnggaran().getRealisasi().doubleValue()), fontstyle));
		        isi6.setBorder(Rectangle.BOX);
		        isi6.setHorizontalAlignment(Element.ALIGN_CENTER);
		        isi6.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(isi6);
		        PdfPCell isi7 = new PdfPCell(new Paragraph(currency(spm.getAnggaran().getSaldo().doubleValue()), fontstyle));
		        isi7.setBorder(Rectangle.BOX);
		        isi7.setHorizontalAlignment(Element.ALIGN_CENTER);
		        isi7.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(isi7);
		        document.add(tbl);
		        
		        Paragraph kosong7 = new Paragraph(" ", fontstyle);
		        document.add(kosong7);
		        
		        Paragraph par2 = new Paragraph("Demikian disampaikan, atas perhatian dan kerjasamanya diucapkan terima kasih.", fontstyle);
		        par2.setAlignment(Element.ALIGN_JUSTIFIED);
		        par2.setFirstLineIndent(30);
		        document.add(par2);
		        
		        Paragraph kosong8 = new Paragraph(" ", fontstyle);
		        document.add(kosong8);
		        Paragraph kosong9 = new Paragraph(" ", fontstyle);
		        document.add(kosong9);
		        
		        PdfPTable table3 = new PdfPTable(2);
		        table3.setWidthPercentage(100);
		        table3.setTotalWidth(new float[] { 50, 50 });
		        PdfPCell cell31 = new PdfPCell();
		        cell31.setBorder(PdfPCell.NO_BORDER);
		        table3.addCell(cell31);
		        PdfPCell cell32 = new PdfPCell(new Paragraph(spmTemplate.getDari(), fontstyle));
		        cell32.setBorder(PdfPCell.NO_BORDER);
		        cell32.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table3.addCell(cell32);
		        
		        PdfPCell cell35 = new PdfPCell(new Paragraph(" ", fontstyle));
		        cell35.setBorder(PdfPCell.NO_BORDER);
		        table3.addCell(cell35);
		        PdfPCell cell36 = new PdfPCell(new Paragraph(" ", fontstyle));
		        cell36.setBorder(PdfPCell.NO_BORDER);
		        table3.addCell(cell36);
		        PdfPCell cell37 = new PdfPCell(new Paragraph(" ", fontstyle));
		        cell37.setBorder(PdfPCell.NO_BORDER);
		        table3.addCell(cell37);
		        PdfPCell cell38 = new PdfPCell(new Paragraph(" ", fontstyle));
		        cell38.setBorder(PdfPCell.NO_BORDER);
		        table3.addCell(cell38);
		        PdfPCell cell39 = new PdfPCell(new Paragraph(" ", fontstyle));
		        cell39.setBorder(PdfPCell.NO_BORDER);
		        table3.addCell(cell39);
		        PdfPCell cell310 = new PdfPCell(new Paragraph(" ", fontstyle));
		        cell310.setBorder(PdfPCell.NO_BORDER);
		        table3.addCell(cell310);
		        
		        PdfPCell cell33 = new PdfPCell();
		        cell33.setBorder(PdfPCell.NO_BORDER);
		        table3.addCell(cell33);
		        PdfPCell cell34 = new PdfPCell(new Paragraph(spmTemplate.getDari_deputi(), fontstyle));
		        cell34.setBorder(PdfPCell.NO_BORDER);
		        cell34.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table3.addCell(cell34);
		        document.add(table3);
		        
			    document.close();
			    writer.close();
			    
			    File file = new File(path + filename);
			    response = Response.ok((Object) file);
	        	response.header("Content-Disposition", "attachment; filename=" + filename+"_"+spm.getKode()+".pdf");
				return response.build();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
	        response = Response.noContent();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/spm/lembur/{token}/{kode}/{useract}/{kodeoffice}")
	@Produces({"application/pdf","application/json"})
	public Response cetakSPMLembur(@Context HttpHeaders headers, 
			@PathParam("token") String token, 
			@PathParam("kode") Integer kode, 
			@PathParam("useract") Integer useract,
			@PathParam("kodeoffice") String kodeoffice) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		
		if (SharedMethod.VerifyToken2(token, metadata)) {
			ResponseBuilder response;
			Connection con = null;
			ResultSet rs = null;
			PreparedStatement ps = null;
			CallableStatement cs = null;
			String query = null;
			
			SpmTemplate spmTemplate = null;
			Spm spm = null;
			
			try {
				query = "select top 1 kode, kepada, dari, dari_deputi, tempat from cuti.spmtemplate where kodeoffice=? and row_status=1";
				con = new Koneksi().getConnection();
				ps = con.prepareStatement(query);
				ps.setString(1, kodeoffice);
				rs = ps.executeQuery();
				if(rs.next()) {
					spmTemplate = new SpmTemplate();
					spmTemplate.setKode(rs.getInt("kode"));
					spmTemplate.setKepada(rs.getString("kepada"));
					spmTemplate.setDari(rs.getString("dari"));
					spmTemplate.setDari_deputi(rs.getString("dari_deputi"));
					spmTemplate.setTempat(rs.getString("tempat"));
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
					}
				}
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
					}
				}
			}
			
			String kodeprogram = null, kodeakun = null, namalengkap = null;
			if(spmTemplate!=null) {
				spm = new Spm();
				
				BigDecimal total = new BigDecimal(0);
				try {
					query = "select a.kode, a.nomor, a.kodeprogram, a.kodeakun, isnull(b.total,0) as total, d.namalengkap, g.namalengkap as pejabat, g.namajabatan as jabatan \r\n" + 
							"from lembur.lembur a \r\n" + 
							"inner join lembur.pegawai b on a.kode=b.kodelembur \r\n" + 
							"inner join karyawan.penugasan c on b.kodepenugasan=c.kode \r\n" + 
							"inner join karyawan.vw_pegawai d on c.npp=d.npp \r\n" + 
							"inner join karyawan.penugasan e on a.pembuat=e.kode \r\n" + 
							"inner join organisasi.hirarkijabatan f on e.kodehirarkijabatan=f.kode \r\n" + 
							"outer apply\r\n" + 
							"( \r\n" + 
							"	select * from organisasi.get_pimpinanbykantor(e.kodeoffice,f.kodeunitkerja) \r\n" + 
							") g \r\n" + 
							"where a.kode=? order by b.kode";
					con = new Koneksi().getConnection();
					ps = con.prepareStatement(query);
					ps.setInt(1, kode);
					rs = ps.executeQuery();
					Integer i=0;
					while(rs.next()) {
						if(i==0) {
							namalengkap = rs.getString("namalengkap");
						}
						kodeprogram = rs.getString("kodeprogram");
						kodeakun = rs.getString("kodeakun");
						spm.setNomor(rs.getString("nomor"));
						total = total.add(rs.getBigDecimal("total"));
						
						spmTemplate.setDari(rs.getString("jabatan"));
						spmTemplate.setDari_deputi(rs.getString("pejabat"));
						i++;
					}
					if(i>1) {
						namalengkap = namalengkap.concat(" Dkk");
					}
					spm.setTotal(total);
					spm.setKepada(namalengkap);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e) {
						}
					}
					if (ps != null) {
						try {
							ps.close();
						} catch (SQLException e) {
						}
					}
					if (con != null) {
						try {
							con.close();
						} catch (SQLException e) {
						}
					}
				}
				
				try {
					int _tahun = Calendar.getInstance().get(Calendar.YEAR);
					int _bulan = Calendar.getInstance().get(Calendar.MONTH)+1;
					int _hari = Calendar.getInstance().get(Calendar.DATE);
					spmTemplate.setTanggal(_hari);
					spmTemplate.setBulan(_bulan);
					spmTemplate.setTahun(_tahun);
					
					query = "exec referensi.sp_getanggaran ?,?,?,?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, _tahun);
					cs.setString(2, kodeoffice);
					cs.setString(3, kodeprogram);
					cs.setString(4, kodeakun);
					rs = cs.executeQuery();
					if(rs.next()) {
						Anggaran anggaran = new Anggaran();
						anggaran.setTahun(rs.getInt("tahun"));
						anggaran.setAlokasi(rs.getBigDecimal("alokasi"));
						anggaran.setRealisasi(rs.getBigDecimal("realisasi"));
						anggaran.setSaldo(rs.getBigDecimal("saldo"));
						Office office = new Office();
						office.setKode(rs.getString("kodeoffice"));
						office.setNama(rs.getString("namaoffice"));
						anggaran.setOffice(office);
						Program program = new Program();
						program.setKode(rs.getString("kodeprogram"));
						program.setNama(rs.getString("namaprogram"));
						anggaran.setProgram(program);
						Akun akun = new Akun();
						akun.setKode(rs.getString("kodeakun"));
						akun.setNama(rs.getString("namaakun"));
						anggaran.setAkun(akun);
						
						spm.setAnggaran(anggaran);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e) {
						}
					}
					if (cs != null) {
						try {
							cs.close();
						} catch (SQLException e) {
						}
					}
					if (con != null) {
						try {
							con.close();
						} catch (SQLException e) {
						}
					}
				}
				
				try {
					query = "insert into lembur.spm(kodelembur,kodespmtemplate,total,kepada,tahunanggaran,kodeprogram,kodeakun,alokasianggaran,realisasianggaran,saldoanggaran,created_by) values (?,?,?,?,?,?,?,?,?,?,?)";
					con = new Koneksi().getConnection();
					ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					ps.setInt(1, kode);
					ps.setInt(2, spmTemplate.getKode());
					ps.setBigDecimal(3, total);
					ps.setString(4, spm.getKepada());
					ps.setInt(5, spm.getAnggaran().getTahun());
					ps.setString(6, spm.getAnggaran().getProgram().getKode());
					ps.setString(7, spm.getAnggaran().getAkun().getKode());
					ps.setBigDecimal(8, spm.getAnggaran().getAlokasi());
					ps.setBigDecimal(9, spm.getAnggaran().getRealisasi());
					ps.setBigDecimal(10, spm.getAnggaran().getSaldo());
					ps.setInt(11, useract);
					ps.execute();
					rs = ps.getGeneratedKeys();
					int generatedKey = 0;
					if (rs.next()) {
					    generatedKey = rs.getInt(1);
					}
					spm.setKode(generatedKey);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e) {
						}
					}
					if (ps != null) {
						try {
							ps.close();
						} catch (SQLException e) {
						}
					}
					if (con != null) {
						try {
							con.close();
						} catch (SQLException e) {
						}
					}
				}
				
			}
			else {
				metadata.setCode(0);
				metadata.setMessage("Setting template SPM terlebih dahulu.");
				result.setMetadata(metadata);
				return Response.ok(result).build();
			}
			
			
			String[] bulan={"","Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","Nopember","Desember"};
			
			Document document = new Document(PageSize.A4, 70, 70, 120, 80);
			try {
				String path = "/tmp/";
				String filename = "SPM_Lembur";
				PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path + filename));
			    document.open();
			    
			    Font fontstyle = FontFactory.getFont("Times Roman", 10, BaseColor.BLACK);
			    
			    PdfPTable table = new PdfPTable(3);
			    table.setWidthPercentage(100);
			    table.setTotalWidth(new float[] { 15, 50, 35 });
		        PdfPCell cell1 = new PdfPCell(new Paragraph("Nomor", fontstyle));
		        cell1.setBorder(PdfPCell.NO_BORDER);
		        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell1);
		        PdfPCell cell2 = new PdfPCell(new Paragraph(": ", fontstyle));
		        cell2.setBorder(PdfPCell.NO_BORDER);
		        cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell2);
		        PdfPCell cell3 = new PdfPCell(new Paragraph(spmTemplate.getTempat()+", "+spmTemplate.getTanggal()+" "+bulan[spmTemplate.getBulan()]+" "+spmTemplate.getTahun(), fontstyle));
		        cell3.setBorder(PdfPCell.NO_BORDER);
		        cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        table.addCell(cell3);
		        
		        PdfPCell cell4 = new PdfPCell(new Paragraph("Lampiran", fontstyle));
		        cell4.setBorder(PdfPCell.NO_BORDER);
		        cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell4);
		        PdfPCell cell5 = new PdfPCell(new Paragraph(": Satu berkas", fontstyle));
		        cell5.setBorder(PdfPCell.NO_BORDER);
		        cell5.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell5);
		        PdfPCell cell6 = new PdfPCell();
		        cell6.setBorder(PdfPCell.NO_BORDER);
		        cell6.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell6);
		        
		        PdfPCell cell7 = new PdfPCell(new Paragraph("Hal", fontstyle));
		        cell7.setBorder(PdfPCell.NO_BORDER);
		        cell7.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell7);
		        PdfPCell cell8 = new PdfPCell(new Paragraph(": Surat Perintah Pembayaran Lembur", fontstyle));
		        cell8.setBorder(PdfPCell.NO_BORDER);
		        cell8.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell8);
		        PdfPCell cell9 = new PdfPCell();
		        cell9.setBorder(PdfPCell.NO_BORDER);
		        cell9.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell9);
		        
		        PdfPCell cell10 = new PdfPCell(new Paragraph("", fontstyle));
		        cell10.setBorder(PdfPCell.NO_BORDER);
		        cell10.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell10);
		        PdfPCell cell11 = new PdfPCell(new Paragraph("  Uang Makan dan Transport", fontstyle));
		        cell11.setBorder(PdfPCell.NO_BORDER);
		        cell11.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell11);
		        PdfPCell cell12 = new PdfPCell();
		        cell12.setBorder(PdfPCell.NO_BORDER);
		        cell12.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell12);
		        
		        document.add(table);
		        
		        Paragraph kosong1 = new Paragraph(" ", fontstyle);
		        document.add(kosong1);
		        
		        
		        Paragraph baris1 = new Paragraph("Yth. "+spmTemplate.getKepada(), fontstyle);
		        Paragraph baris2 = new Paragraph("BPJS Kesehatan", fontstyle);
		        Paragraph baris3 = new Paragraph("di", fontstyle);
		        Paragraph baris4 = new Paragraph(spmTemplate.getTempat(), fontstyle);
		        document.add(baris1);
		        document.add(baris2);
		        document.add(baris3);
		        document.add(baris4);
		        
		        Paragraph kosong2 = new Paragraph(" ", fontstyle);
		        document.add(kosong2);
		        
		        Paragraph par1 = new Paragraph("Saya yang bertanda tangan dibawah ini selaku "+spmTemplate.getDari()+" memerintahkan kepada "+spmTemplate.getKepada()+" untuk melakukan pembayaran :", fontstyle);
		        par1.setAlignment(Element.ALIGN_JUSTIFIED);
		        par1.setFirstLineIndent(30);
		        document.add(par1);
		        
		        Paragraph kosong3 = new Paragraph(" ", fontstyle);
		        document.add(kosong3);
		        
		        PdfPTable table2 = new PdfPTable(3);
		        table2.setWidthPercentage(100);
		        table2.setTotalWidth(new float[] { 20, 2, 68 });
		        PdfPCell cell21 = new PdfPCell(new Paragraph("Sejumlah", fontstyle));
		        cell21.setBorder(PdfPCell.NO_BORDER);
		        cell21.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell21);
		        PdfPCell cell22 = new PdfPCell(new Paragraph(":", fontstyle));
		        cell22.setBorder(PdfPCell.NO_BORDER);
		        cell22.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell22);
		        PdfPCell cell23 = new PdfPCell(new Paragraph(kursInfo(spm.getTotal().doubleValue()), fontstyle));
		        cell23.setBorder(PdfPCell.NO_BORDER);
		        cell23.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell23);
		        
		        PdfPCell cell24 = new PdfPCell(new Paragraph("Dengan huruf", fontstyle));
		        cell24.setBorder(PdfPCell.NO_BORDER);
		        cell24.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell24);
		        PdfPCell cell25 = new PdfPCell(new Paragraph(":", fontstyle));
		        cell25.setBorder(PdfPCell.NO_BORDER);
		        cell25.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell25);
		        PdfPCell cell26 = new PdfPCell(new Paragraph(kapital(terbilang(spm.getTotal().longValue())+" rupiah"), fontstyle));
		        cell26.setBorder(PdfPCell.NO_BORDER);
		        cell26.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		        table2.addCell(cell26);
		        
		        PdfPCell cell27 = new PdfPCell(new Paragraph("Kepada", fontstyle));
		        cell27.setBorder(PdfPCell.NO_BORDER);
		        cell27.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell27);
		        PdfPCell cell28 = new PdfPCell(new Paragraph(":", fontstyle));
		        cell28.setBorder(PdfPCell.NO_BORDER);
		        cell28.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell28);
		        PdfPCell cell29 = new PdfPCell(new Paragraph(kapitalFul(spm.getKepada()), fontstyle));
		        cell29.setBorder(PdfPCell.NO_BORDER);
		        cell29.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell29);
		        
		        PdfPCell cell210 = new PdfPCell(new Paragraph("Untuk pembayaran", fontstyle));
		        cell210.setBorder(PdfPCell.NO_BORDER);
		        cell210.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell210);
		        PdfPCell cell211 = new PdfPCell(new Paragraph(":", fontstyle));
		        cell211.setBorder(PdfPCell.NO_BORDER);
		        cell211.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell211);
		        PdfPCell cell212 = new PdfPCell(new Paragraph("Uang Lembur, Uang Makan dan Transport berdasarkan Surat Perintah Lembur Nomor "+spm.getNomor(), fontstyle));
		        cell212.setBorder(PdfPCell.NO_BORDER);
		        cell212.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell212);
		        
		        document.add(table2);
		        
		        Paragraph kosong4 = new Paragraph(" ", fontstyle);
		        document.add(kosong4);
		        
		        Paragraph atasdasar = new Paragraph("Atas dasar :", fontstyle);
		        document.add(atasdasar);
		        Paragraph atasdasar1 = new Paragraph("1. Kuitansi", fontstyle);
		        atasdasar1.setIndentationLeft(20);
		        document.add(atasdasar1);
		        Paragraph atasdasar2 = new Paragraph("2. Laporan Kegiatan Lembur", fontstyle);
		        atasdasar2.setIndentationLeft(20);
		        document.add(atasdasar2);
		        
		        Paragraph kosong5 = new Paragraph(" ", fontstyle);
		        document.add(kosong5);
		        
		        Paragraph beban = new Paragraph("Dibebankan pada :", fontstyle);
		        document.add(beban);
		        
		        Paragraph kosong6 = new Paragraph(" ", fontstyle);
		        document.add(kosong6);
		        
		        PdfPTable tbl = new PdfPTable(7);
		        tbl.setWidthPercentage(100);
		        tbl.setTotalWidth(new float[] { 5, 26, 12, 12, 15, 15, 15 });
		        PdfPCell c1 = new PdfPCell(new Paragraph("No", fontstyle));
		        c1.setBorder(Rectangle.BOX);
		        c1.setRowspan(2);
		        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(c1);
		        PdfPCell c2 = new PdfPCell(new Paragraph("Mata Anggaran", fontstyle));
		        c2.setBorder(Rectangle.BOX);
		        c2.setRowspan(2);
		        c2.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(c2);
		        PdfPCell c3 = new PdfPCell(new Paragraph("Kode", fontstyle));
		        c3.setBorder(Rectangle.BOX);
		        c3.setColspan(2);
		        c3.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(c3);
		        PdfPCell c4 = new PdfPCell(new Paragraph("Alokasi Anggaran Tahun "+spm.getAnggaran().getTahun(), fontstyle));
		        c4.setBorder(Rectangle.BOX);
		        c4.setRowspan(2);
		        c4.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(c4);
		        PdfPCell c5 = new PdfPCell(new Paragraph("Realisasi Anggaran", fontstyle));
		        c5.setBorder(Rectangle.BOX);
		        c5.setRowspan(2);
		        c5.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c5.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(c5);
		        PdfPCell c6 = new PdfPCell(new Paragraph("Saldo Anggaran", fontstyle));
		        c6.setBorder(Rectangle.BOX);
		        c6.setRowspan(2);
		        c6.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c6.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(c6);
		        PdfPCell c7 = new PdfPCell(new Paragraph("Program", fontstyle));
		        c7.setBorder(Rectangle.BOX);
		        c7.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c7.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(c7);
		        PdfPCell c8 = new PdfPCell(new Paragraph("Akun", fontstyle));
		        c8.setBorder(Rectangle.BOX);
		        c8.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c8.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(c8);
		        //-----------------------------------
		        PdfPCell isi1 = new PdfPCell(new Paragraph("1", fontstyle));
		        isi1.setBorder(Rectangle.BOX);
		        isi1.setHorizontalAlignment(Element.ALIGN_CENTER);
		        isi1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(isi1);
		        PdfPCell isi2 = new PdfPCell(new Paragraph(kapitalFul(spm.getAnggaran().getProgram().getNama())+" - "+kapitalFul(spm.getAnggaran().getAkun().getNama()), fontstyle));
		        isi2.setBorder(Rectangle.BOX);
		        isi2.setHorizontalAlignment(Element.ALIGN_CENTER);
		        isi2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(isi2);
		        PdfPCell isi3 = new PdfPCell(new Paragraph(spm.getAnggaran().getProgram().getKode(), fontstyle));
		        isi3.setBorder(Rectangle.BOX);
		        isi3.setHorizontalAlignment(Element.ALIGN_CENTER);
		        isi3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(isi3);
		        PdfPCell isi4 = new PdfPCell(new Paragraph(spm.getAnggaran().getAkun().getKode(), fontstyle));
		        isi4.setBorder(Rectangle.BOX);
		        isi4.setHorizontalAlignment(Element.ALIGN_CENTER);
		        isi4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(isi4);
		        PdfPCell isi5 = new PdfPCell(new Paragraph(currency(spm.getAnggaran().getAlokasi().doubleValue()), fontstyle));
		        isi5.setBorder(Rectangle.BOX);
		        isi5.setHorizontalAlignment(Element.ALIGN_CENTER);
		        isi5.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(isi5);
		        PdfPCell isi6 = new PdfPCell(new Paragraph(currency(spm.getAnggaran().getRealisasi().doubleValue()), fontstyle));
		        isi6.setBorder(Rectangle.BOX);
		        isi6.setHorizontalAlignment(Element.ALIGN_CENTER);
		        isi6.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(isi6);
		        PdfPCell isi7 = new PdfPCell(new Paragraph(currency(spm.getAnggaran().getSaldo().doubleValue()), fontstyle));
		        isi7.setBorder(Rectangle.BOX);
		        isi7.setHorizontalAlignment(Element.ALIGN_CENTER);
		        isi7.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(isi7);
		        document.add(tbl);
		        
		        Paragraph kosong7 = new Paragraph(" ", fontstyle);
		        document.add(kosong7);
		        
		        Paragraph par2 = new Paragraph("Demikian disampaian, atas perhatian dan kerjasamanya diucapkan terima kasih.", fontstyle);
		        par2.setAlignment(Element.ALIGN_JUSTIFIED);
		        par2.setFirstLineIndent(30);
		        document.add(par2);
		        
		        Paragraph kosong8 = new Paragraph(" ", fontstyle);
		        document.add(kosong8);
		        Paragraph kosong9 = new Paragraph(" ", fontstyle);
		        document.add(kosong9);
		        
		        PdfPTable table3 = new PdfPTable(2);
		        table3.setWidthPercentage(100);
		        table3.setTotalWidth(new float[] { 50, 50 });
		        PdfPCell cell31 = new PdfPCell();
		        cell31.setBorder(PdfPCell.NO_BORDER);
		        table3.addCell(cell31);
		        PdfPCell cell32 = new PdfPCell(new Paragraph(spmTemplate.getDari(), fontstyle));
		        cell32.setBorder(PdfPCell.NO_BORDER);
		        cell32.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table3.addCell(cell32);
		        
		        PdfPCell cell35 = new PdfPCell(new Paragraph(" ", fontstyle));
		        cell35.setBorder(PdfPCell.NO_BORDER);
		        table3.addCell(cell35);
		        PdfPCell cell36 = new PdfPCell(new Paragraph(" ", fontstyle));
		        cell36.setBorder(PdfPCell.NO_BORDER);
		        table3.addCell(cell36);
		        PdfPCell cell37 = new PdfPCell(new Paragraph(" ", fontstyle));
		        cell37.setBorder(PdfPCell.NO_BORDER);
		        table3.addCell(cell37);
		        PdfPCell cell38 = new PdfPCell(new Paragraph(" ", fontstyle));
		        cell38.setBorder(PdfPCell.NO_BORDER);
		        table3.addCell(cell38);
		        PdfPCell cell39 = new PdfPCell(new Paragraph(" ", fontstyle));
		        cell39.setBorder(PdfPCell.NO_BORDER);
		        table3.addCell(cell39);
		        PdfPCell cell310 = new PdfPCell(new Paragraph(" ", fontstyle));
		        cell310.setBorder(PdfPCell.NO_BORDER);
		        table3.addCell(cell310);
		        
		        PdfPCell cell33 = new PdfPCell();
		        cell33.setBorder(PdfPCell.NO_BORDER);
		        table3.addCell(cell33);
		        PdfPCell cell34 = new PdfPCell(new Paragraph(spmTemplate.getDari_deputi(), fontstyle));
		        cell34.setBorder(PdfPCell.NO_BORDER);
		        cell34.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table3.addCell(cell34);
		        document.add(table3);
		        
			    document.close();
			    writer.close();
			    
			    File file = new File(path + filename);
			    response = Response.ok((Object) file);
	        	response.header("Content-Disposition", "attachment; filename=" + filename+"_"+spm.getKode()+".pdf");
				return response.build();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
	        response = Response.noContent();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/spm/skpd/{token}/{kode}/{useract}/{jeniskantor}/{kodelokasi}/{kodeoffice}")
	@Produces({"application/pdf","application/json"})
	public Response cetakSPMSkpd(@Context HttpHeaders headers, 
			@PathParam("token") String token, 
			@PathParam("kode") Integer kode, 
			@PathParam("useract") Integer useract,
			@PathParam("jeniskantor") Integer jeniskantor,
			@PathParam("kodelokasi") String kodelokasi,
			@PathParam("kodeoffice") String kodeoffice) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		
		if (SharedMethod.VerifyToken2(token, metadata)) {
			ResponseBuilder response;
			Connection con = null;
			ResultSet rs = null;
			ResultSet rs2 = null;
			ResultSet rs3 = null;
			PreparedStatement ps = null;
			CallableStatement cs = null;
			CallableStatement cs2 = null;
			CallableStatement cs3 = null;
			String query = null;
			
			SpmTemplate spmTemplate = null;
			Spm spm = null;
			
			try {
				query = "select top 1 kode, kepada, dari, dari_deputi, tempat from cuti.spmtemplate where kodeoffice=? and row_status=1";
				con = new Koneksi().getConnection();
				ps = con.prepareStatement(query);
				ps.setString(1, kodeoffice);
				rs = ps.executeQuery();
				if(rs.next()) {
					spmTemplate = new SpmTemplate();
					spmTemplate.setKode(rs.getInt("kode"));
					spmTemplate.setKepada(rs.getString("kepada"));
					spmTemplate.setDari(rs.getString("dari"));
					spmTemplate.setDari_deputi(rs.getString("dari_deputi"));
					spmTemplate.setTempat(rs.getString("tempat"));
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
					}
				}
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
					}
				}
			}
			
			String kodeprogram = null, kodeakun = null, namalengkap = null;
			if(spmTemplate!=null) {
				spm = new Spm();
				
				Integer kodeskpdpegawai = 0;
				BigDecimal total = new BigDecimal(0);
				try {
					query = "exec skpd.sp_getlaporanskpdpegawaidetail ?,?,?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query);
					cs.setInt(1, kode);
					cs.setInt(2, jeniskantor);
					cs.setString(3, kodelokasi);
					rs = cs.executeQuery();
					Integer i=0;
					while(rs.next()) {
						/*-----*/
						try {
							query = "exec skpd.sp_getlaporanskpdpegawaidetailtagihan ?";
							cs2 = con.prepareCall(query);
							cs2.setInt(1, rs.getInt("kode"));
							rs2 = cs2.executeQuery();
							while(rs2.next()) {
								/*-----*/
								try {
									query = "exec skpd.sp_getlaporanskpdpegawaidetailtagihandetail ?, ?";
									cs3 = con.prepareCall(query);
									cs3.setInt(1, rs.getInt("kode"));
									cs3.setShort(2, rs2.getShort("jenis"));
									rs3 = cs3.executeQuery();
									while(rs3.next()) {
										total = total.add(rs3.getBigDecimal("subtotal"));
									}
								}
								catch (Exception e) {
									e.printStackTrace();
								}
								finally {
									if (rs3 != null) {
										try {
											rs3.close();
										} catch (SQLException e) {
										}
									}
									if (cs3 != null) {
										try {
											cs3.close();
										} catch (SQLException e) {
										}
									}
								}
								/*-----*/
							}
						}
						catch (Exception e) {
							e.printStackTrace();
						}
						finally {
							if (rs2 != null) {
								try {
									rs2.close();
								} catch (SQLException e) {
								}
							}
							if (cs2 != null) {
								try {
									cs2.close();
								} catch (SQLException e) {
								}
							}
						}
						/*-----*/
						if(i==0) {
							kodeskpdpegawai = rs.getInt("kode");
							namalengkap = rs.getString("namalengkap");
						}
						i++;
					}
					if(i>1) {
						namalengkap = namalengkap.concat(" Dkk");
					}
					spm.setTotal(total);
					spm.setKepada(namalengkap);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e) {
						}
					}
					if (cs != null) {
						try {
							cs.close();
						} catch (SQLException e) {
						}
					}
					if (con != null) {
						try {
							con.close();
						} catch (SQLException e) {
						}
					}
				}
				
				try {
					query = "select \r\n" + 
							"a.nomor, \r\n" + 
							"a.kodeakun, \r\n" + 
							"a.kodeprogram, \r\n" + 
							"e.namalengkap as ttdpejabat, \r\n" + 
							"e.namajabatan as ttbjabatan \r\n" + 
							"from skpd.skpd a \r\n" + 
							"inner join skpd.skpdpegawai b on a.kode=b.kodeskpd \r\n" + 
							"inner join karyawan.penugasan c on b.kodepenugasan=c.kode \r\n" + 
							"inner join organisasi.hirarkijabatan d on c.kodehirarkijabatan=d.kode \r\n" + 
							"outer apply \r\n" + 
							"( \r\n" + 
							"	select * from organisasi.get_pimpinanbykantor(c.kodeoffice,d.kodeunitkerja) \r\n" + 
							") e \r\n" + 
							"where a.kode=? and b.kode=?";
					con = new Koneksi().getConnection();
					ps = con.prepareStatement(query);
					ps.setInt(1, kode);
					ps.setInt(2, kodeskpdpegawai);
					rs = ps.executeQuery();
					Integer i=0;
					while(rs.next()) {
						kodeprogram = rs.getString("kodeprogram");
						kodeakun = rs.getString("kodeakun");
						spm.setNomor(rs.getString("nomor"));
						spmTemplate.setDari(rs.getString("ttbjabatan"));
						spmTemplate.setDari_deputi(rs.getString("ttdpejabat"));
						i++;
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e) {
						}
					}
					if (ps != null) {
						try {
							ps.close();
						} catch (SQLException e) {
						}
					}
					if (con != null) {
						try {
							con.close();
						} catch (SQLException e) {
						}
					}
				}
				
				try {
					int _tahun = Calendar.getInstance().get(Calendar.YEAR);
					int _bulan = Calendar.getInstance().get(Calendar.MONTH)+1;
					int _hari = Calendar.getInstance().get(Calendar.DATE);
					spmTemplate.setTanggal(_hari);
					spmTemplate.setBulan(_bulan);
					spmTemplate.setTahun(_tahun);
					
					query = "exec referensi.sp_getanggaran ?,?,?,?";
					con = new Koneksi().getConnection();
					cs = con.prepareCall(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
					cs.setInt(1, _tahun);
					cs.setString(2, kodeoffice);
					cs.setString(3, kodeprogram);
					cs.setString(4, kodeakun);
					rs = cs.executeQuery();
					if(rs.next()) {
						Anggaran anggaran = new Anggaran();
						anggaran.setTahun(rs.getInt("tahun"));
						anggaran.setAlokasi(rs.getBigDecimal("alokasi"));
						anggaran.setRealisasi(rs.getBigDecimal("realisasi"));
						anggaran.setSaldo(rs.getBigDecimal("saldo"));
						Office office = new Office();
						office.setKode(rs.getString("kodeoffice"));
						office.setNama(rs.getString("namaoffice"));
						anggaran.setOffice(office);
						Program program = new Program();
						program.setKode(rs.getString("kodeprogram"));
						program.setNama(rs.getString("namaprogram"));
						anggaran.setProgram(program);
						Akun akun = new Akun();
						akun.setKode(rs.getString("kodeakun"));
						akun.setNama(rs.getString("namaakun"));
						anggaran.setAkun(akun);
						
						spm.setAnggaran(anggaran);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e) {
						}
					}
					if (cs != null) {
						try {
							cs.close();
						} catch (SQLException e) {
						}
					}
					if (con != null) {
						try {
							con.close();
						} catch (SQLException e) {
						}
					}
				}
				
				try {
					query = "insert into skpd.spm(kodeskpd,kodespmtemplate,total,kepada,tahunanggaran,kodeprogram,kodeakun,alokasianggaran,realisasianggaran,saldoanggaran,created_by) values (?,?,?,?,?,?,?,?,?,?,?)";
					con = new Koneksi().getConnection();
					ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					ps.setInt(1, kode);
					ps.setInt(2, spmTemplate.getKode());
					ps.setBigDecimal(3, total);
					ps.setString(4, spm.getKepada());
					ps.setInt(5, spm.getAnggaran().getTahun());
					ps.setString(6, spm.getAnggaran().getProgram().getKode());
					ps.setString(7, spm.getAnggaran().getAkun().getKode());
					ps.setBigDecimal(8, spm.getAnggaran().getAlokasi());
					ps.setBigDecimal(9, spm.getAnggaran().getRealisasi());
					ps.setBigDecimal(10, spm.getAnggaran().getSaldo());
					ps.setInt(11, useract);
					ps.execute();
					rs = ps.getGeneratedKeys();
					int generatedKey = 0;
					if (rs.next()) {
					    generatedKey = rs.getInt(1);
					}
					spm.setKode(generatedKey);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e) {
						}
					}
					if (ps != null) {
						try {
							ps.close();
						} catch (SQLException e) {
						}
					}
					if (con != null) {
						try {
							con.close();
						} catch (SQLException e) {
						}
					}
				}
				
			}
			else {
				metadata.setCode(0);
				metadata.setMessage("Setting template SPM terlebih dahulu.");
				result.setMetadata(metadata);
				return Response.ok(result).build();
			}
			
			
			String[] bulan={"","Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","Nopember","Desember"};
			
			Document document = new Document(PageSize.A4, 70, 70, 120, 80);
			try {
				String path = "/tmp/";
				String filename = "SPM_SKPD";
				PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path + filename));
			    document.open();
			    
			    Font fontstyle = FontFactory.getFont("Times Roman", 10, BaseColor.BLACK);
			    
			    PdfPTable table = new PdfPTable(3);
			    table.setWidthPercentage(100);
			    table.setTotalWidth(new float[] { 15, 50, 35 });
		        PdfPCell cell1 = new PdfPCell(new Paragraph("Nomor", fontstyle));
		        cell1.setBorder(PdfPCell.NO_BORDER);
		        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell1);
		        PdfPCell cell2 = new PdfPCell(new Paragraph(": ", fontstyle));
		        cell2.setBorder(PdfPCell.NO_BORDER);
		        cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell2);
		        PdfPCell cell3 = new PdfPCell(new Paragraph(spmTemplate.getTempat()+", "+spmTemplate.getTanggal()+" "+bulan[spmTemplate.getBulan()]+" "+spmTemplate.getTahun(), fontstyle));
		        cell3.setBorder(PdfPCell.NO_BORDER);
		        cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        table.addCell(cell3);
		        
		        PdfPCell cell4 = new PdfPCell(new Paragraph("Lampiran", fontstyle));
		        cell4.setBorder(PdfPCell.NO_BORDER);
		        cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell4);
		        PdfPCell cell5 = new PdfPCell(new Paragraph(": Satu berkas", fontstyle));
		        cell5.setBorder(PdfPCell.NO_BORDER);
		        cell5.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell5);
		        PdfPCell cell6 = new PdfPCell();
		        cell6.setBorder(PdfPCell.NO_BORDER);
		        cell6.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell6);
		        
		        PdfPCell cell7 = new PdfPCell(new Paragraph("Hal", fontstyle));
		        cell7.setBorder(PdfPCell.NO_BORDER);
		        cell7.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell7);
		        PdfPCell cell8 = new PdfPCell(new Paragraph(": Surat Perintah Pembayaran Uang Saku", fontstyle));
		        cell8.setBorder(PdfPCell.NO_BORDER);
		        cell8.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell8);
		        PdfPCell cell9 = new PdfPCell();
		        cell9.setBorder(PdfPCell.NO_BORDER);
		        cell9.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell9);
		        
		        PdfPCell cell10 = new PdfPCell(new Paragraph("", fontstyle));
		        cell10.setBorder(PdfPCell.NO_BORDER);
		        cell10.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell10);
		        PdfPCell cell11 = new PdfPCell(new Paragraph("  Perjalanan Dinas", fontstyle));
		        cell11.setBorder(PdfPCell.NO_BORDER);
		        cell11.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell11);
		        PdfPCell cell12 = new PdfPCell();
		        cell12.setBorder(PdfPCell.NO_BORDER);
		        cell12.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell12);
		        
		        document.add(table);
		        
		        Paragraph kosong1 = new Paragraph(" ", fontstyle);
		        document.add(kosong1);
		        
		        
		        Paragraph baris1 = new Paragraph("Yth. "+spmTemplate.getKepada(), fontstyle);
		        Paragraph baris2 = new Paragraph("BPJS Kesehatan", fontstyle);
		        Paragraph baris3 = new Paragraph("di", fontstyle);
		        Paragraph baris4 = new Paragraph(spmTemplate.getTempat(), fontstyle);
		        document.add(baris1);
		        document.add(baris2);
		        document.add(baris3);
		        document.add(baris4);
		        
		        Paragraph kosong2 = new Paragraph(" ", fontstyle);
		        document.add(kosong2);
		        
		        Paragraph par1 = new Paragraph("Saya yang bertanda tangan dibawah ini selaku "+spmTemplate.getDari()+" memerintahkan kepada "+spmTemplate.getKepada()+" untuk melakukan pembayaran :", fontstyle);
		        par1.setAlignment(Element.ALIGN_JUSTIFIED);
		        par1.setFirstLineIndent(30);
		        document.add(par1);
		        
		        Paragraph kosong3 = new Paragraph(" ", fontstyle);
		        document.add(kosong3);
		        
		        PdfPTable table2 = new PdfPTable(3);
		        table2.setWidthPercentage(100);
		        table2.setTotalWidth(new float[] { 20, 2, 68 });
		        PdfPCell cell21 = new PdfPCell(new Paragraph("Sejumlah", fontstyle));
		        cell21.setBorder(PdfPCell.NO_BORDER);
		        cell21.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell21);
		        PdfPCell cell22 = new PdfPCell(new Paragraph(":", fontstyle));
		        cell22.setBorder(PdfPCell.NO_BORDER);
		        cell22.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell22);
		        PdfPCell cell23 = new PdfPCell(new Paragraph(kursInfo(spm.getTotal().doubleValue()), fontstyle));
		        cell23.setBorder(PdfPCell.NO_BORDER);
		        cell23.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell23);
		        
		        PdfPCell cell24 = new PdfPCell(new Paragraph("Dengan huruf", fontstyle));
		        cell24.setBorder(PdfPCell.NO_BORDER);
		        cell24.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell24);
		        PdfPCell cell25 = new PdfPCell(new Paragraph(":", fontstyle));
		        cell25.setBorder(PdfPCell.NO_BORDER);
		        cell25.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell25);
		        PdfPCell cell26 = new PdfPCell(new Paragraph(kapital(terbilang(spm.getTotal().longValue())+" rupiah"), fontstyle));
		        cell26.setBorder(PdfPCell.NO_BORDER);
		        cell26.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		        table2.addCell(cell26);
		        
		        PdfPCell cell27 = new PdfPCell(new Paragraph("Kepada", fontstyle));
		        cell27.setBorder(PdfPCell.NO_BORDER);
		        cell27.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell27);
		        PdfPCell cell28 = new PdfPCell(new Paragraph(":", fontstyle));
		        cell28.setBorder(PdfPCell.NO_BORDER);
		        cell28.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell28);
		        PdfPCell cell29 = new PdfPCell(new Paragraph(kapitalFul(spm.getKepada()), fontstyle));
		        cell29.setBorder(PdfPCell.NO_BORDER);
		        cell29.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell29);
		        
		        PdfPCell cell210 = new PdfPCell(new Paragraph("Untuk pembayaran", fontstyle));
		        cell210.setBorder(PdfPCell.NO_BORDER);
		        cell210.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell210);
		        PdfPCell cell211 = new PdfPCell(new Paragraph(":", fontstyle));
		        cell211.setBorder(PdfPCell.NO_BORDER);
		        cell211.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell211);
		        PdfPCell cell212 = new PdfPCell(new Paragraph("Uang Saku Perjalanan Dinas Nomor "+spm.getNomor(), fontstyle));
		        cell212.setBorder(PdfPCell.NO_BORDER);
		        cell212.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table2.addCell(cell212);
		        
		        document.add(table2);
		        
		        Paragraph kosong4 = new Paragraph(" ", fontstyle);
		        document.add(kosong4);
		        
		        Paragraph atasdasar = new Paragraph("Atas dasar :", fontstyle);
		        document.add(atasdasar);
		        Paragraph atasdasar1 = new Paragraph("1. Kuitansi", fontstyle);
		        atasdasar1.setIndentationLeft(20);
		        document.add(atasdasar1);
		        Paragraph atasdasar2 = new Paragraph("2. Laporan Kegiatan SKPD", fontstyle);
		        atasdasar2.setIndentationLeft(20);
		        document.add(atasdasar2);
		        
		        Paragraph kosong5 = new Paragraph(" ", fontstyle);
		        document.add(kosong5);
		        
		        Paragraph beban = new Paragraph("Dibebankan pada :", fontstyle);
		        document.add(beban);
		        
		        Paragraph kosong6 = new Paragraph(" ", fontstyle);
		        document.add(kosong6);
		        
		        PdfPTable tbl = new PdfPTable(7);
		        tbl.setWidthPercentage(100);
		        tbl.setTotalWidth(new float[] { 5, 26, 12, 12, 15, 15, 15 });
		        PdfPCell c1 = new PdfPCell(new Paragraph("No", fontstyle));
		        c1.setBorder(Rectangle.BOX);
		        c1.setRowspan(2);
		        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(c1);
		        PdfPCell c2 = new PdfPCell(new Paragraph("Mata Anggaran", fontstyle));
		        c2.setBorder(Rectangle.BOX);
		        c2.setRowspan(2);
		        c2.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(c2);
		        PdfPCell c3 = new PdfPCell(new Paragraph("Kode", fontstyle));
		        c3.setBorder(Rectangle.BOX);
		        c3.setColspan(2);
		        c3.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(c3);
		        PdfPCell c4 = new PdfPCell(new Paragraph("Alokasi Anggaran Tahun "+spm.getAnggaran().getTahun(), fontstyle));
		        c4.setBorder(Rectangle.BOX);
		        c4.setRowspan(2);
		        c4.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(c4);
		        PdfPCell c5 = new PdfPCell(new Paragraph("Realisasi Anggaran", fontstyle));
		        c5.setBorder(Rectangle.BOX);
		        c5.setRowspan(2);
		        c5.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c5.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(c5);
		        PdfPCell c6 = new PdfPCell(new Paragraph("Saldo Anggaran", fontstyle));
		        c6.setBorder(Rectangle.BOX);
		        c6.setRowspan(2);
		        c6.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c6.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(c6);
		        PdfPCell c7 = new PdfPCell(new Paragraph("Program", fontstyle));
		        c7.setBorder(Rectangle.BOX);
		        c7.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c7.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(c7);
		        PdfPCell c8 = new PdfPCell(new Paragraph("Akun", fontstyle));
		        c8.setBorder(Rectangle.BOX);
		        c8.setHorizontalAlignment(Element.ALIGN_CENTER);
		        c8.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(c8);
		        //-----------------------------------
		        PdfPCell isi1 = new PdfPCell(new Paragraph("1", fontstyle));
		        isi1.setBorder(Rectangle.BOX);
		        isi1.setHorizontalAlignment(Element.ALIGN_CENTER);
		        isi1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(isi1);
		        PdfPCell isi2 = new PdfPCell(new Paragraph(kapitalFul(spm.getAnggaran().getProgram().getNama())+" - "+kapitalFul(spm.getAnggaran().getAkun().getNama()), fontstyle));
		        isi2.setBorder(Rectangle.BOX);
		        isi2.setHorizontalAlignment(Element.ALIGN_CENTER);
		        isi2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(isi2);
		        PdfPCell isi3 = new PdfPCell(new Paragraph(spm.getAnggaran().getProgram().getKode(), fontstyle));
		        isi3.setBorder(Rectangle.BOX);
		        isi3.setHorizontalAlignment(Element.ALIGN_CENTER);
		        isi3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(isi3);
		        PdfPCell isi4 = new PdfPCell(new Paragraph(spm.getAnggaran().getAkun().getKode(), fontstyle));
		        isi4.setBorder(Rectangle.BOX);
		        isi4.setHorizontalAlignment(Element.ALIGN_CENTER);
		        isi4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(isi4);
		        PdfPCell isi5 = new PdfPCell(new Paragraph(currency(spm.getAnggaran().getAlokasi().doubleValue()), fontstyle));
		        isi5.setBorder(Rectangle.BOX);
		        isi5.setHorizontalAlignment(Element.ALIGN_CENTER);
		        isi5.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(isi5);
		        PdfPCell isi6 = new PdfPCell(new Paragraph(currency(spm.getAnggaran().getRealisasi().doubleValue()), fontstyle));
		        isi6.setBorder(Rectangle.BOX);
		        isi6.setHorizontalAlignment(Element.ALIGN_CENTER);
		        isi6.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(isi6);
		        PdfPCell isi7 = new PdfPCell(new Paragraph(currency(spm.getAnggaran().getSaldo().doubleValue()), fontstyle));
		        isi7.setBorder(Rectangle.BOX);
		        isi7.setHorizontalAlignment(Element.ALIGN_CENTER);
		        isi7.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        tbl.addCell(isi7);
		        document.add(tbl);
		        
		        Paragraph kosong7 = new Paragraph(" ", fontstyle);
		        document.add(kosong7);
		        
		        Paragraph par2 = new Paragraph("Demikian disampaian, atas perhatian dan kerjasamanya diucapkan terima kasih.", fontstyle);
		        par2.setAlignment(Element.ALIGN_JUSTIFIED);
		        par2.setFirstLineIndent(30);
		        document.add(par2);
		        
		        Paragraph kosong8 = new Paragraph(" ", fontstyle);
		        document.add(kosong8);
		        Paragraph kosong9 = new Paragraph(" ", fontstyle);
		        document.add(kosong9);
		        
		        PdfPTable table3 = new PdfPTable(2);
		        table3.setWidthPercentage(100);
		        table3.setTotalWidth(new float[] { 50, 50 });
		        PdfPCell cell31 = new PdfPCell();
		        cell31.setBorder(PdfPCell.NO_BORDER);
		        table3.addCell(cell31);
		        PdfPCell cell32 = new PdfPCell(new Paragraph(spmTemplate.getDari(), fontstyle));
		        cell32.setBorder(PdfPCell.NO_BORDER);
		        cell32.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table3.addCell(cell32);
		        
		        PdfPCell cell35 = new PdfPCell(new Paragraph(" ", fontstyle));
		        cell35.setBorder(PdfPCell.NO_BORDER);
		        table3.addCell(cell35);
		        PdfPCell cell36 = new PdfPCell(new Paragraph(" ", fontstyle));
		        cell36.setBorder(PdfPCell.NO_BORDER);
		        table3.addCell(cell36);
		        PdfPCell cell37 = new PdfPCell(new Paragraph(" ", fontstyle));
		        cell37.setBorder(PdfPCell.NO_BORDER);
		        table3.addCell(cell37);
		        PdfPCell cell38 = new PdfPCell(new Paragraph(" ", fontstyle));
		        cell38.setBorder(PdfPCell.NO_BORDER);
		        table3.addCell(cell38);
		        PdfPCell cell39 = new PdfPCell(new Paragraph(" ", fontstyle));
		        cell39.setBorder(PdfPCell.NO_BORDER);
		        table3.addCell(cell39);
		        PdfPCell cell310 = new PdfPCell(new Paragraph(" ", fontstyle));
		        cell310.setBorder(PdfPCell.NO_BORDER);
		        table3.addCell(cell310);
		        
		        PdfPCell cell33 = new PdfPCell();
		        cell33.setBorder(PdfPCell.NO_BORDER);
		        table3.addCell(cell33);
		        PdfPCell cell34 = new PdfPCell(new Paragraph(spmTemplate.getDari_deputi(), fontstyle));
		        cell34.setBorder(PdfPCell.NO_BORDER);
		        cell34.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table3.addCell(cell34);
		        document.add(table3);
		        
			    document.close();
			    writer.close();
			    
			    File file = new File(path + filename);
			    response = Response.ok((Object) file);
	        	response.header("Content-Disposition", "attachment; filename=" + filename+"_"+spm.getKode()+".pdf");
				return response.build();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
	        response = Response.noContent();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/{jenis}/{token}/{kode}")
	@Produces("application/pdf")
	public Response cetakSKPD(@Context HttpHeaders headers, @PathParam("jenis") String jenis, @PathParam("token") String token, @PathParam("kode") Integer kode) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		
		if (SharedMethod.VerifyToken2(token, metadata)) {
			ResponseBuilder response;
			Connection con = null;
			String sumber = "";
			String filename = "";
			
			try {
				String ihc_baseurl = context.getInitParameter("ihc.baseurl");
				
				if(jenis.equalsIgnoreCase("skpd")) {
					filename = "SKPD";				
					sumber = "/IHC-Report/eskapede.jasper";					
				} else if(jenis.equalsIgnoreCase("lembur")) {
					filename = "LEMBUR";				
					sumber = "/IHC-Report/lembur.jasper";
				}
				
				String path = "/tmp/";
				con = new Koneksi().getConnection();
				
				HashMap hm = new HashMap();
				hm.put("kode", kode);
				hm.put("baseurl", ihc_baseurl);

				File report_file = new File(sumber);
	            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(report_file.getPath());
	            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, hm, con);
	            JasperExportManager.exportReportToPdfFile(jasperPrint,path + filename + "_" + kode + ".pdf");
			    
			    File file = new File(path + filename + "_" + kode + ".pdf");
			    response = Response.ok((Object) file);
	        	response.header("Content-Disposition", "attachment; filename=" + filename+"_"+kode+".pdf");
				return response.build();
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
					}
				}
			}
			
	        response = Response.noContent();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/payroll/{nmfile}/{token}/{npp}/{tahun}/{bulan}")
	@Produces("application/pdf")
	public Response cetakPayroll(@Context HttpHeaders headers, @PathParam("token") String token, @PathParam("npp") String npp, @PathParam("tahun") Integer tahun, @PathParam("bulan") Integer bulan, @PathParam("nmfile") String nmfile) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		
		if (SharedMethod.VerifyToken2(token, metadata)) {
			ResponseBuilder response;
			Connection con = null;
			String sumber = "";
			String filename = "";
			
			try {
				String ihc_baseurl = context.getInitParameter("ihc.baseurl");
				
				filename = "SLIP";				
				sumber = "/IHC-Report/SlipPayroll.jasper";
				
				String path = "/tmp/";
				con = new Koneksi().getConnection();
				
				HashMap hm = new HashMap();
				hm.put("npp", npp);
				hm.put("bulan", bulan);
				hm.put("tahun", tahun);
				hm.put("baseurl", ihc_baseurl);

				File report_file = new File(sumber);
	            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(report_file.getPath());
	            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, hm, con);
	            jasperPrint.setProperty("net.sf.jasperreports.export.pdf.permissions.allowed", "PRINTING");
	            jasperPrint.setProperty("net.sf.jasperreports.export.pdf.user.password", nmfile);
	            JasperExportManager.exportReportToPdfFile(jasperPrint,path + filename + "_" + npp + "_" + bulan + "_" + tahun + ".pdf");
			    
			    File file = new File(path + filename + "_" + npp +"_" + bulan + "_" + tahun + ".pdf");
			    response = Response.ok((Object) file);
	        	response.header("Content-Disposition", "attachment; filename=" + filename+"_"+npp+"_" + bulan + "_" + tahun +".pdf");
				return response.build();
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
					}
				}
			}
			
	        response = Response.noContent();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
	@GET
	@Path("/cv/{token}/{npp}")
	@Produces("application/pdf")
	public Response cetakCV(
			@Context HttpHeaders headers, 
			@PathParam("token") String token, 
			@PathParam("npp") String npp
		) {
		Result<Object> result = new Result<Object>();
		Metadata metadata = new Metadata();
		
		if (SharedMethod.VerifyToken2(token, metadata)) {
			ResponseBuilder response;
			Connection con = null;
			String sumber = "";
			String filename = "CV";
			
			try {				
				sumber = "/IHC-Report/CV.jasper";
				
				String path = "/tmp/";
				con = new Koneksi().getConnection();
				
				HashMap hm = new HashMap();
				hm.put("npp", npp);

				File report_file = new File(sumber);
	            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(report_file.getPath());
	            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, hm, con);
	            JasperExportManager.exportReportToPdfFile(jasperPrint,path + filename + "_" + npp + ".pdf");
			    
			    File file = new File(path + filename + "_" + npp + ".pdf");
			    response = Response.ok((Object) file);
	        	response.header("Content-Disposition", "attachment; filename=" + filename + "_" + npp +".pdf");
				return response.build();
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
					}
				}
			}
			
	        response = Response.noContent();
			return response.build();
		}
		result.setMetadata(metadata);
		return Response.ok(result).build();
	}
	
}
