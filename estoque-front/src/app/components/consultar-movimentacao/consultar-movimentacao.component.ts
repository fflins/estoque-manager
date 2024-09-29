import { Component, OnInit } from '@angular/core';
import { MovimentacaoService } from '../../services/movimentacao.service';
import { CommonModule } from '@angular/common';
import { Movimentacao } from '../../models/movimentacao.model';
import { DatePipe } from '@angular/common';
import { Location } from '@angular/common';

@Component({
  selector: 'app-consultar-movimentacoes',
  templateUrl: './consultar-movimentacao.component.html',
  standalone: true,
  imports: [CommonModule, DatePipe],
})

export class ConsultarMovimentacaoComponent implements OnInit {
  movimentacoes: Movimentacao[] = [];

  constructor(private movimentacaoService: MovimentacaoService, private location: Location) {}

  voltar(): void {
    this.location.back();
  }

  ngOnInit(): void {
    this.consultarMovimentacoes();
  }

  consultarMovimentacoes() {
    this.movimentacaoService.consultarMovimentacoes().subscribe(movimentacoes => {
      this.movimentacoes = movimentacoes.map(mov => ({
        ...mov,
        dataHora: new Date(mov.dataHora) // Converte a string para um objeto Date
      }));
    });
  }


}