package id.go.bpjskesehatan.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import id.go.bpjskesehatan.entitas.kinerja.Bobotkomponen;
import id.go.bpjskesehatan.entitas.kinerja.Gruppenilai;
import id.go.bpjskesehatan.entitas.kinerja.Gruppenilaijobtitle;
import id.go.bpjskesehatan.entitas.kinerja.Gruppenilaikomposisi;
import id.go.bpjskesehatan.entitas.kinerja.Interpretasi;
import id.go.bpjskesehatan.entitas.kinerja.Komponen;
import id.go.bpjskesehatan.entitas.kinerja.Kriteria;
import id.go.bpjskesehatan.entitas.kinerja.Masterkomunikasi;
import id.go.bpjskesehatan.entitas.kinerja.Masterkriteriapenilaian;
import id.go.bpjskesehatan.entitas.kinerja.Masternilai;
import id.go.bpjskesehatan.entitas.kinerja.Masternilaidetail;
import id.go.bpjskesehatan.entitas.kinerja.Penilai;
import id.go.bpjskesehatan.entitas.kinerja.Periodekinerja;
import id.go.bpjskesehatan.entitas.kinerja.Periodekinerjadetail;
import id.go.bpjskesehatan.entitas.kinerja.Periodeubkomitmen;
import id.go.bpjskesehatan.entitas.kinerja.Periodeubkompetensi;
import id.go.bpjskesehatan.entitas.kinerja.Peserta;
import id.go.bpjskesehatan.entitas.kinerja.Siklus;
import id.go.bpjskesehatan.entitas.kinerja.Ubkomitmenkomunikasi;
import id.go.bpjskesehatan.entitas.kinerja.Ubkomitmenkriteriapenilaian;
import id.go.bpjskesehatan.entitas.kinerja.Ubkomitmennilai;
import id.go.bpjskesehatan.entitas.kinerja.Ubkompetensikomunikasi;
import id.go.bpjskesehatan.entitas.kinerja.Ubkompetensikriteriapenilaian;

@Path("/kinerja")
public class KinerjaRest {
	@POST
	@Path("/{servicename}/list/{page}/{row}")
	@Produces("application/json")
	public Response getListData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			@PathParam("page") String page, @PathParam("row") String row, String data) {
		String query = null;
		String namasp = null;
		String namaentitas = null;
		Object obj = null;
		namasp = "sp_list" + servicename;
		namaentitas = servicename;
		switch (servicename) {
		case "bobotkomponen":
			obj = new Bobotkomponen();
			break;
		case "gruppenilai":
			obj = new Gruppenilai();
			break;
		case "gruppenilaijobtitle":
			obj = new Gruppenilaijobtitle();
			break;
		case "gruppenilaikomposisi":
			obj = new Gruppenilaikomposisi();
			break;
		case "interpretasi":
			obj = new Interpretasi();
			break;
		case "komponen":
			obj = new Komponen();
			break;
		case "kriteria":
			obj = new Kriteria();
			break;
		case "penilai":
			obj = new Penilai();
			break;
		case "periodekinerja":
			obj = new Periodekinerja();
			break;
		case "periodekinerjadetail":
			obj = new Periodekinerjadetail();
			break;
		case "peserta":
			obj = new Peserta();
			break;
		case "siklus":
			obj = new Siklus();
			break;
		case "masterkomunikasi":
			obj = new Masterkomunikasi();
			break;
		case "masterkriteriapenilaian":
			obj = new Masterkriteriapenilaian();
			break;
		case "masternilai":
			obj = new Masternilai();
			break;
		case "masternilaidetail":
			obj = new Masternilaidetail();
			break;
		case "periodeubkomitmen":
			obj = new Periodeubkomitmen();
			break;
		case "periodeubkompetensi":
			obj = new Periodeubkompetensi();
			break;
		case "ubkomitmenkomunikasi":
			obj = new Ubkomitmenkomunikasi();
			break;
		case "ubkomitmenkriteriapenilaian":
			obj = new Ubkomitmenkriteriapenilaian();
			break;
		case "ubkomitmennilai":
			obj = new Ubkomitmennilai();
			break;
		case "ubkompetensikomunikasi":
			obj = new Ubkompetensikomunikasi();
			break;
		case "ubkompetensikriteriapenilaian":
			obj = new Ubkompetensikriteriapenilaian();
			break;
		case "kpi":
		case "kriteriakpi":
		case "rencanaaktifitas":
		case "kompetensi":
		case "komitment":
		case "tugastambahan":
		case "kejadiankritis":
			query = "exec kinerja." + namasp + " ?, ?, ?, ?, ?";
			return RestMethod.getListDataUsingMap(headers, page, row, query, data);
		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		query = "exec kinerja." + namasp + " ?, ?, ?, ?, ?";
		return RestMethod.getListData(headers, page, row, namaentitas, obj, query, data);
	}

	@POST
	@Path("/{servicename}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response createData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			String data) {

		Object obj = null;
		String namatabel = null;
		namatabel = "kinerja." + servicename;
		switch (servicename) {
		case "bobotkomponen":
			obj = new Bobotkomponen();
			break;
		case "gruppenilai":
			obj = new Gruppenilai();
			break;
		case "gruppenilaijobtitle":
			obj = new Gruppenilaijobtitle();
			break;
		case "gruppenilaikomposisi":
			obj = new Gruppenilaikomposisi();
			break;
		case "interpretasi":
			obj = new Interpretasi();
			break;
		case "komponen":
			obj = new Komponen();
			break;
		case "kriteria":
			obj = new Kriteria();
			break;
		case "penilai":
			obj = new Penilai();
			break;
		case "periodekinerja":
			obj = new Periodekinerja();
			break;
		case "periodekinerjadetail":
			obj = new Periodekinerjadetail();
			break;
		case "peserta":
			obj = new Peserta();
			break;
		case "siklus":
			obj = new Siklus();
			break;
		case "masterkomunikasi":
			obj = new Masterkomunikasi();
			break;
		case "masterkriteriapenilaian":
			obj = new Masterkriteriapenilaian();
			break;
		case "masternilai":
			obj = new Masternilai();
			break;
		case "masternilaidetail":
			obj = new Masternilaidetail();
			break;
		case "periodeubkomitmen":
			obj = new Periodeubkomitmen();
			break;
		case "periodeubkompetensi":
			obj = new Periodeubkompetensi();
			break;
		case "ubkomitmenkomunikasi":
			obj = new Ubkomitmenkomunikasi();
			break;
		case "ubkomitmenkriteriapenilaian":
			obj = new Ubkomitmenkriteriapenilaian();
			break;
		case "ubkomitmennilai":
			obj = new Ubkomitmennilai();
			break;
		case "ubkompetensikomunikasi":
			obj = new Ubkompetensikomunikasi();
			break;
		case "ubkompetensikriteriapenilaian":
			obj = new Ubkompetensikriteriapenilaian();
			break;
		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		return RestMethod.createData(headers, data, obj, namatabel);
	}

	@PUT
	@Path("/{servicename}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response updateData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			String data) {

		Object obj = null;
		String namatabel = null;
		namatabel = "kinerja." + servicename;
		switch (servicename) {
		case "bobotkomponen":
			obj = new Bobotkomponen();
			break;
		case "gruppenilai":
			obj = new Gruppenilai();
			break;
		case "gruppenilaijobtitle":
			obj = new Gruppenilaijobtitle();
			break;
		case "gruppenilaikomposisi":
			obj = new Gruppenilaikomposisi();
			break;
		case "interpretasi":
			obj = new Interpretasi();
			break;
		case "komponen":
			obj = new Komponen();
			break;
		case "kriteria":
			obj = new Kriteria();
			break;
		case "penilai":
			obj = new Penilai();
			break;
		case "periodekinerja":
			obj = new Periodekinerja();
			break;
		case "periodekinerjadetail":
			obj = new Periodekinerjadetail();
			break;
		case "peserta":
			obj = new Peserta();
			break;
		case "siklus":
			obj = new Siklus();
			break;
		case "masterkomunikasi":
			obj = new Masterkomunikasi();
			break;
		case "masterkriteriapenilaian":
			obj = new Masterkriteriapenilaian();
			break;
		case "masternilai":
			obj = new Masternilai();
			break;
		case "masternilaidetail":
			obj = new Masternilaidetail();
			break;
		case "periodeubkomitmen":
			obj = new Periodeubkomitmen();
			break;
		case "periodeubkompetensi":
			obj = new Periodeubkompetensi();
			break;
		case "ubkomitmenkomunikasi":
			obj = new Ubkomitmenkomunikasi();
			break;
		case "ubkomitmenkriteriapenilaian":
			obj = new Ubkomitmenkriteriapenilaian();
			break;
		case "ubkomitmennilai":
			obj = new Ubkomitmennilai();
			break;
		case "ubkompetensikomunikasi":
			obj = new Ubkompetensikomunikasi();
			break;
		case "ubkompetensikriteriapenilaian":
			obj = new Ubkompetensikriteriapenilaian();
			break;
		case "kpi":
		case "kriteriakpi":
		case "rencanaaktifitas":
		case "kompetensi":
		case "komitment":
		case "tugastambahan":
		case "kejadiankritis":
			return RestMethod.updateData(headers, data, namatabel);
		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		return RestMethod.updateData(headers, data, obj, namatabel);
	}

	@POST
	@Path("/{servicename}/delete")
	@Consumes("application/json")
	@Produces("application/json")
	public Response deleteData(@Context HttpHeaders headers, @PathParam("servicename") String servicename,
			String data) {

		Object obj = null;
		String namatabel = null;
		namatabel = "kinerja." + servicename;
		switch (servicename) {
		case "bobotkomponen":
			obj = new Bobotkomponen();
			break;
		case "gruppenilai":
			obj = new Gruppenilai();
			break;
		case "gruppenilaijobtitle":
			obj = new Gruppenilaijobtitle();
			break;
		case "gruppenilaikomposisi":
			obj = new Gruppenilaikomposisi();
			break;
		case "interpretasi":
			obj = new Interpretasi();
			break;
		case "komponen":
			obj = new Komponen();
			break;
		case "kriteria":
			obj = new Kriteria();
			break;
		case "penilai":
			obj = new Penilai();
			break;
		case "periodekinerja":
			obj = new Periodekinerja();
			break;
		case "periodekinerjadetail":
			obj = new Periodekinerjadetail();
			break;
		case "peserta":
			obj = new Peserta();
			break;
		case "siklus":
			obj = new Siklus();
			break;
		case "masterkomunikasi":
			obj = new Masterkomunikasi();
			break;
		case "masterkriteriapenilaian":
			obj = new Masterkriteriapenilaian();
			break;
		case "masternilai":
			obj = new Masternilai();
			break;
		case "masternilaidetail":
			obj = new Masternilaidetail();
			break;
		case "periodeubkomitmen":
			obj = new Periodeubkomitmen();
			break;
		case "periodeubkompetensi":
			obj = new Periodeubkompetensi();
			break;
		case "ubkomitmenkomunikasi":
			obj = new Ubkomitmenkomunikasi();
			break;
		case "ubkomitmenkriteriapenilaian":
			obj = new Ubkomitmenkriteriapenilaian();
			break;
		case "ubkomitmennilai":
			obj = new Ubkomitmennilai();
			break;
		case "ubkompetensikomunikasi":
			obj = new Ubkompetensikomunikasi();
			break;
		case "ubkompetensikriteriapenilaian":
			obj = new Ubkompetensikriteriapenilaian();
			break;
		default:
			return Response.status(Status.NOT_FOUND).build();
		}
		return RestMethod.deleteData(headers, data, obj, namatabel);
	}
}
