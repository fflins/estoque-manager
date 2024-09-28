import { Component } from '@angular/core';
import { InserirProdutoComponent } from './components/inserir-produto/inserir-produto.component';
import { ConsultarProdutosComponent } from './components/consultar-produtos/consultar-produtos.component';
import { RouterModule } from '@angular/router'
import { RemoverProdutoComponent } from './components/remover-produto/remover-produto.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  standalone: true,
  imports: [RouterModule, InserirProdutoComponent, ConsultarProdutosComponent, RemoverProdutoComponent],
})
export class AppComponent {
  title = 'estoque-front';
}