package com.corhuila.sgie;

import com.corhuila.sgie.Equipment.Entity.CategoriaEquipo;
import com.corhuila.sgie.Equipment.IRepository.ICategoriaEquipoRepository;
import com.corhuila.sgie.Equipment.IRepository.ITipoEquipoRepository;
import com.corhuila.sgie.Equipment.Service.CategoriaEquipoService;
import com.corhuila.sgie.Equipment.Service.TipoEquipoService;
import com.corhuila.sgie.Maintenance.Entity.CategoriaMantenimientoEquipo;
import com.corhuila.sgie.Maintenance.Entity.CategoriaMantenimientoInstalacion;
import com.corhuila.sgie.Maintenance.IRepository.ICategoriaMantenimientoEquipoRepository;
import com.corhuila.sgie.Maintenance.IRepository.ICategoriaMantenimientoInstalacionRepository;
import com.corhuila.sgie.Maintenance.Service.CategoriaMantenimientoEquipoService;
import com.corhuila.sgie.Maintenance.Service.CategoriaMantenimientoInstalacionService;
import com.corhuila.sgie.Site.Entity.*;
import com.corhuila.sgie.Site.IRepository.*;
import com.corhuila.sgie.Site.Service.*;
import com.corhuila.sgie.User.DTO.IPermisoPorPersonaDTO;
import com.corhuila.sgie.User.DTO.IPermisoRolEntidadDTO;
import com.corhuila.sgie.User.Entity.Entidad;
import com.corhuila.sgie.User.Entity.Permiso;
import com.corhuila.sgie.User.Entity.Persona;
import com.corhuila.sgie.User.Entity.Rol;
import com.corhuila.sgie.User.IRepository.*;
import com.corhuila.sgie.User.Service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogServiceSmokeTest {

    @Mock
    private ICategoriaEquipoRepository categoriaEquipoRepository;
    @Mock
    private ITipoEquipoRepository tipoEquipoRepository;
    @Mock
    private ICategoriaMantenimientoEquipoRepository categoriaMantEquipoRepository;
    @Mock
    private ICategoriaMantenimientoInstalacionRepository categoriaMantInstRepository;
    @Mock
    private ICategoriaInstalacionRepository categoriaInstalacionRepository;
    @Mock
    private IMunicipioRepository municipioRepository;
    @Mock
    private IPaisRepository paisRepository;
    @Mock
    private IDepartamentoRepository departamentoRepository;
    @Mock
    private IContinenteRepository continenteRepository;
    @Mock
    private IInstalacionRepository instalacionRepository;
    @Mock
    private IPermisoRepository permisoRepository;
    @Mock
    private IRolRepository rolRepository;
    @Mock
    private IPersonaRepository personaRepository;
    @Mock
    private IEntidadRepository entidadRepository;
    @Mock
    private IPermisoRolEntidadRepository permisoRolEntidadRepository;


    private CategoriaEquipoService categoriaEquipoService;
    private CategoriaMantenimientoEquipoService categoriaMantEquipoService;
    private CategoriaMantenimientoInstalacionService categoriaMantInstService;
    private CategoriaInstalacionService categoriaInstalacionService;
    private MunicipioService municipioService;
    private PaisService paisService;
    private DepartamentoService departamentoService;
    private ContinenteService continenteService;
    private InstalacionService instalacionService;
    private TipoEquipoService tipoEquipoService;
    private PermisoService permisoService;
    private RolService rolService;
    private PersonaService personaService;
    private EntidadService entidadService;
    private PermisoRolEntidadService permisoRolEntidadService;

    @BeforeEach
    void init() {
        categoriaEquipoService = new CategoriaEquipoService(categoriaEquipoRepository);
        ReflectionTestUtils.setField(categoriaEquipoService, "repository", categoriaEquipoRepository);

        tipoEquipoService = new TipoEquipoService(tipoEquipoRepository);
        ReflectionTestUtils.setField(tipoEquipoService, "repository", tipoEquipoRepository);

        categoriaMantEquipoService = new CategoriaMantenimientoEquipoService(categoriaMantEquipoRepository);
        ReflectionTestUtils.setField(categoriaMantEquipoService, "repository", categoriaMantEquipoRepository);

        categoriaMantInstService = new CategoriaMantenimientoInstalacionService(categoriaMantInstRepository);
        ReflectionTestUtils.setField(categoriaMantInstService, "repository", categoriaMantInstRepository);

        categoriaInstalacionService = new CategoriaInstalacionService();
        ReflectionTestUtils.setField(categoriaInstalacionService, "repository", categoriaInstalacionRepository);

        municipioService = new MunicipioService(municipioRepository);
        ReflectionTestUtils.setField(municipioService, "repository", municipioRepository);

        paisService = new PaisService(paisRepository);
        ReflectionTestUtils.setField(paisService, "repository", paisRepository);

        departamentoService = new DepartamentoService(departamentoRepository);
        ReflectionTestUtils.setField(departamentoService, "repository", departamentoRepository);

        continenteService = new ContinenteService(continenteRepository);
        ReflectionTestUtils.setField(continenteService, "repository", continenteRepository);

        instalacionService = new InstalacionService(instalacionRepository);
        ReflectionTestUtils.setField(instalacionService, "repository", instalacionRepository);

        permisoService = new PermisoService(permisoRepository);
        ReflectionTestUtils.setField(permisoService, "repository", permisoRepository);

        rolService = new RolService(rolRepository);
        ReflectionTestUtils.setField(rolService, "repository", rolRepository);

        personaService = new PersonaService(personaRepository);
        ReflectionTestUtils.setField(personaService, "repository", personaRepository);

        entidadService = new EntidadService(entidadRepository);
        ReflectionTestUtils.setField(entidadService, "repository", entidadRepository);

        permisoRolEntidadService = new PermisoRolEntidadService(permisoRolEntidadRepository);
        ReflectionTestUtils.setField(permisoRolEntidadService, "repository", permisoRolEntidadRepository);
    }

    @Test
    void catalogosDeleganEnRepositorio() {
        when(categoriaEquipoRepository.findAll()).thenReturn(List.of(new CategoriaEquipo()));
        assertThat(categoriaEquipoService.all()).hasSize(1);

        when(tipoEquipoRepository.findAll()).thenReturn(List.of(new com.corhuila.sgie.Equipment.Entity.TipoEquipo()));
        assertThat(tipoEquipoService.all()).hasSize(1);

        when(categoriaMantEquipoRepository.findAll()).thenReturn(List.of(new CategoriaMantenimientoEquipo()));
        assertThat(categoriaMantEquipoService.all()).hasSize(1);

        when(categoriaMantInstRepository.findAll()).thenReturn(List.of(new CategoriaMantenimientoInstalacion()));
        assertThat(categoriaMantInstService.all()).hasSize(1);

        when(categoriaInstalacionRepository.findAll()).thenReturn(List.of(new CategoriaInstalacion()));
        assertThat(categoriaInstalacionService.all()).hasSize(1);

        when(municipioRepository.findAll()).thenReturn(List.of(new Municipio()));
        assertThat(municipioService.all()).hasSize(1);

        when(paisRepository.findAll()).thenReturn(List.of(new Pais()));
        assertThat(paisService.all()).hasSize(1);

        when(departamentoRepository.findAll()).thenReturn(List.of(new Departamento()));
        assertThat(departamentoService.all()).hasSize(1);

        when(continenteRepository.findAll()).thenReturn(List.of(new Continente()));
        assertThat(continenteService.all()).hasSize(1);

        when(instalacionRepository.findAll()).thenReturn(List.of(new Instalacion()));
        assertThat(instalacionService.all()).hasSize(1);

        when(permisoRepository.findAll()).thenReturn(List.of(new Permiso()));
        assertThat(permisoService.all()).hasSize(1);

        when(rolRepository.findAll()).thenReturn(List.of(new Rol()));
        assertThat(rolService.all()).hasSize(1);

        when(personaRepository.findAll()).thenReturn(List.of(new Persona()));
        assertThat(personaService.all()).hasSize(1);

        when(entidadRepository.findAll()).thenReturn(List.of(new Entidad()));
        assertThat(entidadService.all()).hasSize(1);
    }

    @Test
    void permisoRolEntidadServiceConsultaCustom() {
        IPermisoPorPersonaDTO permisoPersona = org.mockito.Mockito.mock(IPermisoPorPersonaDTO.class);
        when(permisoRolEntidadRepository.findPermisosPorNumeroIdentificacion(anyString()))
                .thenReturn(List.of(permisoPersona));
        assertThat(permisoRolEntidadService.obtenerPermisos("123")).containsExactly(permisoPersona);

        IPermisoRolEntidadDTO permisoRolEntidad = org.mockito.Mockito.mock(IPermisoRolEntidadDTO.class);
        when(permisoRolEntidadRepository.findPermisosByRolByEntidad()).thenReturn(List.of(permisoRolEntidad));
        assertThat(permisoRolEntidadService.findPermisosByRolByEntidad()).containsExactly(permisoRolEntidad);
    }
}
