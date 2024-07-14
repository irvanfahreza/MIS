/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2015 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package id.go.bpjskesehatan;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import id.go.bpjskesehatan.service.CutiRest;
import id.go.bpjskesehatan.service.DjpRest;
import id.go.bpjskesehatan.service.DownloadFileService;
import id.go.bpjskesehatan.service.ExportExcelRest;
import id.go.bpjskesehatan.service.HcisRest;
import id.go.bpjskesehatan.service.InfoRest;
import id.go.bpjskesehatan.service.KaryawanRest;
import id.go.bpjskesehatan.service.KinerjaRest;
import id.go.bpjskesehatan.service.KompetensiRest;
import id.go.bpjskesehatan.service.OrganisasiRest;
import id.go.bpjskesehatan.service.ReferensiRest;
import id.go.bpjskesehatan.service.SkpdRest;
import id.go.bpjskesehatan.service.Sso;
import id.go.bpjskesehatan.service.UploadFileService;
import id.go.bpjskesehatan.service.mobile.v1.Absensi;
import id.go.bpjskesehatan.service.mobile.v1.Firebase;
import id.go.bpjskesehatan.service.mobile.v1.LemburRest;
import id.go.bpjskesehatan.service.mobile.v1.NotifikasiRest;
import id.go.bpjskesehatan.service.v2.Action2Rest;
import id.go.bpjskesehatan.service.v2.ActionRest;
import id.go.bpjskesehatan.service.v2.AutocompleteRest;
import id.go.bpjskesehatan.service.v2.ComboboxRest;
import id.go.bpjskesehatan.service.v2.DownloadRest;
import id.go.bpjskesehatan.service.v2.ExcelRest;
import id.go.bpjskesehatan.service.v2.GridRest;
import id.go.bpjskesehatan.service.v2.ListRest;
import id.go.bpjskesehatan.service.v2.PDFRest;
import id.go.bpjskesehatan.service.v2.cuti.Cuti2Rest;
import id.go.bpjskesehatan.service.v2.djp.Djp2Rest;
import id.go.bpjskesehatan.service.v2.hcis.Setting2Rest;
import id.go.bpjskesehatan.service.v2.karyawan.Karyawan2Rest;
import id.go.bpjskesehatan.service.v2.kinerja.Kinerja2Rest;
import id.go.bpjskesehatan.service.v2.kinerja.UbKomitmen2Rest;
import id.go.bpjskesehatan.service.v2.kinerja.UbKompetensi2Rest;
import id.go.bpjskesehatan.service.v2.kompetensi.Kompetensi2Rest;
import id.go.bpjskesehatan.service.v2.lembur.Lembur2Rest;
import id.go.bpjskesehatan.service.v2.organisasi.Organisasi2Rest;
import id.go.bpjskesehatan.service.v2.payroll.PayrollRest;
import id.go.bpjskesehatan.service.v2.promut.MutasiRest;
import id.go.bpjskesehatan.service.v2.promut.PromosiRest;
import id.go.bpjskesehatan.service.v2.promut.RangkaianRest;
import id.go.bpjskesehatan.service.v2.skpd.Skpd2Rest;

@ApplicationPath("/")
public class MyApplication extends ResourceConfig {

	public MyApplication() {
		super(
				
				MultiPartFeature.class, 
				UploadFileService.class, 
				OrganisasiRest.class, 
				InfoRest.class, 
				Sso.class,
				ReferensiRest.class, 
				KaryawanRest.class, 
				KompetensiRest.class, 
				DjpRest.class, 
				KinerjaRest.class,
				SkpdRest.class, 
				HcisRest.class, 
				CutiRest.class, 
				DownloadFileService.class,
				ActionRest.class,
				AutocompleteRest.class,
				ComboboxRest.class,
				GridRest.class,
				ListRest.class,
				Cuti2Rest.class,
				Skpd2Rest.class,
				DownloadRest.class,
				ExportExcelRest.class,
				PDFRest.class,
				ExcelRest.class,
				MutasiRest.class,
				RangkaianRest.class,
				Lembur2Rest.class,
				PromosiRest.class,
				PayrollRest.class,
				Organisasi2Rest.class,
				InfoRest.class,
				id.go.bpjskesehatan.service.mobile.v1.Sso.class,
				Absensi.class,
				Firebase.class,
				id.go.bpjskesehatan.service.absensi.ListRest.class,
				id.go.bpjskesehatan.service.absensi.ActionRest.class,
				NotifikasiRest.class,
				id.go.bpjskesehatan.service.mobile.v1.CutiRest.class,
				id.go.bpjskesehatan.service.mobile.v1.PayrollRest.class,
				id.go.bpjskesehatan.service.mobile.v1.SkpdRest.class,
				LemburRest.class,
				id.go.bpjskesehatan.service.mobile.v1.KaryawanRest.class,
				Karyawan2Rest.class,
				Setting2Rest.class,
				Djp2Rest.class,
				Organisasi2Rest.class,
				Kinerja2Rest.class,
				Action2Rest.class,
				Kompetensi2Rest.class,
				UbKompetensi2Rest.class,
				UbKomitmen2Rest.class
			);
		
		//register(CORSResponseFilter.class);

	}
}
