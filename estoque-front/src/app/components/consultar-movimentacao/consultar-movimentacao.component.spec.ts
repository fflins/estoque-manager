import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsultarMovimentacaoComponent } from './consultar-movimentacao.component';

describe('ConsultarMovimentacaoComponent', () => {
  let component: ConsultarMovimentacaoComponent;
  let fixture: ComponentFixture<ConsultarMovimentacaoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConsultarMovimentacaoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConsultarMovimentacaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
