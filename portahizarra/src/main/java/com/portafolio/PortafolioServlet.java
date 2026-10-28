package com.portafolio;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/PortafolioServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 20,       // 20MB (mayor tamaño para PDFs)
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class PortafolioServlet extends HttpServlet {

    private static final String BASE_UPLOAD_DIR = "/workspaces/repohizarra/portahizarra/uploads";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String selectedSemana = request.getParameter("semana");
        if (selectedSemana == null || selectedSemana.isEmpty()) {
            selectedSemana = "1";
        }

        int semanaNum = 1;
        try {
            semanaNum = Integer.parseInt(selectedSemana);
        } catch (NumberFormatException e) {
            semanaNum = 1;
        }

        File weekFolder = new File(BASE_UPLOAD_DIR + File.separator + "semana_" + semanaNum);
        if (!weekFolder.exists()) {
            weekFolder.mkdirs();
        }

        // Manejar la acción de eliminar archivo
        String action = request.getParameter("action");
        String fileNameToDelete = request.getParameter("archivo");
        if ("delete".equals(action) && fileNameToDelete != null && !fileNameToDelete.trim().isEmpty()) {
            File fileToDelete = new File(weekFolder, fileNameToDelete);
            if (fileToDelete.exists() && fileToDelete.isFile()) {
                fileToDelete.delete();
            }
            response.sendRedirect("PortafolioServlet?semana=" + semanaNum);
            return;
        }

        // Listar archivos guardados (imágenes y PDFs)
        List<String> uploadedFiles = new ArrayList<>();
        File[] files = weekFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && (isImageFile(file.getName()) || isPdfFile(file.getName()))) {
                    uploadedFiles.add(file.getName());
                }
            }
        }

        out.println("<!DOCTYPE html>");
        out.println("<html lang='es'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>Portafolio Anime - Hizarra Samaniego Percy Andy</title>");
        out.println("    <link href='https://fonts.googleapis.com/css2?family=Orbitron:wght@400;700;900&family=Rajdhani:wght@500;700&display=swap' rel='stylesheet'>");
        out.println("    <style>");
        out.println("        :root {");
        out.println("            --card-bg: rgba(13, 10, 26, 0.85);");
        out.println("            --neon-pink: #ff007f;");
        out.println("            --neon-cyan: #00f3ff;");
        out.println("            --neon-purple: #9d00ff;");
        out.println("            --text-color: #e2e8f0;");
        out.println("        }");
        out.println("        * { box-sizing: border-box; margin: 0; padding: 0; font-family: 'Rajdhani', sans-serif; }");
        out.println("        body {");
        out.println("            background: url('fondo.jpg') no-repeat center center fixed;");
        out.println("            background-size: cover;");
        out.println("            color: var(--text-color);");
        out.println("            min-height: 100vh;");
        out.println("            padding: 20px;");
        out.println("        }");
        out.println("        header {");
        out.println("            text-align: center;");
        out.println("            padding: 30px 10px;");
        out.println("            border-bottom: 2px solid var(--neon-cyan);");
        out.println("            box-shadow: 0 0 15px var(--neon-cyan);");
        out.println("            margin-bottom: 30px;");
        out.println("            background: rgba(13, 10, 26, 0.85);");
        out.println("            border-radius: 12px;");
        out.println("            backdrop-filter: blur(5px);");
        out.println("        }");
        out.println("        h1 {");
        out.println("            font-family: 'Orbitron', sans-serif;");
        out.println("            font-size: 2.2rem;");
        out.println("            color: #fff;");
        out.println("            text-shadow: 0 0 10px var(--neon-pink), 0 0 20px var(--neon-pink);");
        out.println("            letter-spacing: 2px;");
        out.println("            text-transform: uppercase;");
        out.println("        }");
        out.println("        .subtitle {");
        out.println("            font-size: 1.2rem;");
        out.println("            color: var(--neon-cyan);");
        out.println("            margin-top: 5px;");
        out.println("            font-weight: 700;");
        out.println("            text-shadow: 0 0 5px var(--neon-cyan);");
        out.println("        }");
        out.println("        .weeks-container {");
        out.println("            display: flex;");
        out.println("            flex-wrap: wrap;");
        out.println("            gap: 12px;");
        out.println("            justify-content: center;");
        out.println("            margin-bottom: 30px;");
        out.println("        }");
        out.println("        .week-btn {");
        out.println("            padding: 12px 20px;");
        out.println("            background: var(--card-bg);");
        out.println("            border: 2px solid var(--neon-purple);");
        out.println("            color: #fff;");
        out.println("            font-family: 'Orbitron', sans-serif;");
        out.println("            font-weight: bold;");
        out.println("            border-radius: 8px;");
        out.println("            text-decoration: none;");
        out.println("            transition: all 0.3s ease;");
        out.println("            box-shadow: 0 0 10px rgba(157, 0, 255, 0.3);");
        out.println("        }");
        out.println("        .week-btn:hover, .week-btn.active {");
        out.println("            background: var(--neon-pink);");
        out.println("            border-color: #fff;");
        out.println("            box-shadow: 0 0 20px var(--neon-pink);");
        out.println("            transform: translateY(-3px) scale(1.05);");
        out.println("        }");
        out.println("        .main-content {");
        out.println("            max-width: 1100px;");
        out.println("            margin: 0 auto;");
        out.println("            background: var(--card-bg);");
        out.println("            border: 1px solid var(--neon-cyan);");
        out.println("            border-radius: 16px;");
        out.println("            padding: 25px;");
        out.println("            box-shadow: 0 0 25px rgba(0, 243, 255, 0.2);");
        out.println("            backdrop-filter: blur(10px);");
        out.println("        }");
        out.println("        .section-title {");
        out.println("            font-family: 'Orbitron', sans-serif;");
        out.println("            color: var(--neon-cyan);");
        out.println("            margin-bottom: 20px;");
        out.println("            border-left: 5px solid var(--neon-pink);");
        out.println("            padding-left: 12px;");
        out.println("            font-size: 1.5rem;");
        out.println("        }");
        out.println("        .upload-card {");
        out.println("            background: rgba(10, 5, 20, 0.6);");
        out.println("            border: 2px dashed var(--neon-purple);");
        out.println("            padding: 20px;");
        out.println("            border-radius: 12px;");
        out.println("            text-align: center;");
        out.println("            margin-bottom: 30px;");
        out.println("        }");
        out.println("        .file-input { display: none; }");
        out.println("        .file-label {");
        out.println("            display: inline-block;");
        out.println("            padding: 10px 25px;");
        out.println("            background: linear-gradient(45deg, var(--neon-purple), var(--neon-pink));");
        out.println("            color: #fff;");
        out.println("            font-weight: bold;");
        out.println("            border-radius: 6px;");
        out.println("            cursor: pointer;");
        out.println("            margin-right: 10px;");
        out.println("            transition: 0.3s;");
        out.println("        }");
        out.println("        .file-label:hover { opacity: 0.9; transform: scale(1.02); }");
        out.println("        .submit-btn {");
        out.println("            padding: 10px 25px;");
        out.println("            background: var(--neon-cyan);");
        out.println("            color: #000;");
        out.println("            font-weight: bold;");
        out.println("            border: none;");
        out.println("            border-radius: 6px;");
        out.println("            cursor: pointer;");
        out.println("            font-family: 'Orbitron', sans-serif;");
        out.println("            transition: 0.3s;");
        out.println("        }");
        out.println("        .submit-btn:hover { box-shadow: 0 0 15px var(--neon-cyan); }");
        out.println("        .gallery-grid {");
        out.println("            display: grid;");
        out.println("            grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));");
        out.println("            gap: 20px;");
        out.println("        }");
        out.println("        .img-card {");
        out.println("            position: relative;");
        out.println("            border-radius: 10px;");
        out.println("            overflow: hidden;");
        out.println("            border: 2px solid rgba(255, 255, 255, 0.1);");
        out.println("            background: #000;");
        out.println("            height: 240px;");
        out.println("            display: flex;");
        out.println("            flex-direction: column;");
        out.println("        }");
        out.println("        .img-card img {");
        out.println("            width: 100%;");
        out.println("            height: 180px;");
        out.println("            object-fit: cover;");
        out.println("        }");
        out.println("        .pdf-card-content {");
        out.println("            width: 100%;");
        out.println("            height: 180px;");
        out.println("            display: flex;");
        out.println("            flex-direction: column;");
        out.println("            align-items: center;");
        out.println("            justify-content: center;");
        out.println("            background: rgba(20, 10, 35, 0.9);");
        out.println("            color: var(--neon-cyan);");
        out.println("            text-decoration: none;");
        out.println("            padding: 10px;");
        out.println("            text-align: center;");
        out.println("        }");
        out.println("        .pdf-card-content span { font-size: 3rem; margin-bottom: 5px; }");
        out.println("        .pdf-card-content p { font-size: 0.85rem; color: #fff; word-break: break-all; }");
        out.println("        .delete-btn {");
        out.println("            background: var(--neon-pink);");
        out.println("            color: #fff;");
        out.println("            text-align: center;");
        out.println("            padding: 8px;");
        out.println("            font-weight: bold;");
        out.println("            text-decoration: none;");
        out.println("            font-size: 0.9rem;");
        out.println("            transition: 0.3s;");
        out.println("        }");
        out.println("        .delete-btn:hover { background: #ff3399; box-shadow: 0 0 10px var(--neon-pink); }");
        out.println("        .empty-msg {");
        out.println("            text-align: center;");
        out.println("            color: #aaa;");
        out.println("            font-style: italic;");
        out.println("            grid-column: 1 / -1;");
        out.println("            padding: 40px;");
        out.println("            font-size: 1.1rem;");
        out.println("        }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <header>");
        out.println("        <h1>Portafolio Digital</h1>");
        out.println("        <div class='subtitle'>Percy Andy Hizarra Samaniego</div>");
        out.println("    </header>");
        
        out.println("    <div class='weeks-container'>");
        for (int i = 1; i <= 10; i++) {
            String activeClass = (i == semanaNum) ? "active" : "";
            out.println("        <a href='PortafolioServlet?semana=" + i + "' class='week-btn " + activeClass + "'>Semana " + i + "</a>");
        }
        out.println("    </div>");

        out.println("    <div class='main-content'>");
        out.println("        <h2 class='section-title'>Trabajos de la Semana " + semanaNum + "</h2>");
        
        // Formulario de Subida
        out.println("        <div class='upload-card'>");
        out.println("            <form action='PortafolioServlet' method='post' enctype='multipart/form-data'>");
        out.println("                <input type='hidden' name='semana' value='" + semanaNum + "' />");
        out.println("                <label for='file-input' class='file-label'>📁 Seleccionar Archivo (Img / PDF)</label>");
        out.println("                <input id='file-input' type='file' name='imagen' class='file-input' accept='image/*,.pdf' required />");
        out.println("                <button type='submit' class='submit-btn'>🚀 Subir Trabajo</button>");
        out.println("            </form>");
        out.println("        </div>");

        // Galería de Archivos
        out.println("        <div class='gallery-grid'>");
        if (uploadedFiles.isEmpty()) {
            out.println("            <div class='empty-msg'>No hay trabajos o archivos subidos en la Semana " + semanaNum + ". ¡Sube el primero arriba!</div>");
        } else {
            for (String fileName : uploadedFiles) {
                // Apunta correctamente al FileServlet (/uploads/...)
                String filePath = "uploads/semana_" + semanaNum + "/" + fileName;
                out.println("            <div class='img-card'>");
                if (isPdfFile(fileName)) {
                    out.println("                <a href='" + filePath + "' target='_blank' class='pdf-card-content'>");
                    out.println("                    <span>📄</span>");
                    out.println("                    <p>" + fileName + "</p>");
                    out.println("                </a>");
                } else {
                    out.println("                <a href='" + filePath + "' target='_blank'>");
                    out.println("                    <img src='" + filePath + "' alt='Trabajo' />");
                    out.println("                </a>");
                }
                out.println("                <a href='PortafolioServlet?semana=" + semanaNum + "&action=delete&archivo=" + fileName + "' class='delete-btn' onclick='return confirm(\"¿Estás seguro de eliminar este archivo?\");'>🗑️ Eliminar</a>");
                out.println("            </div>");
            }
        }
        out.println("        </div>");
        out.println("    </div>");

        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String selectedSemana = request.getParameter("semana");
        if (selectedSemana == null || selectedSemana.isEmpty()) {
            selectedSemana = "1";
        }

        int semanaNum = 1;
        try {
            semanaNum = Integer.parseInt(selectedSemana);
        } catch (NumberFormatException e) {
            semanaNum = 1;
        }

        Part filePart = request.getPart("imagen");
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = getSubmittedFileName(filePart);
            if (fileName != null && !fileName.trim().isEmpty()) {
                File weekFolder = new File(BASE_UPLOAD_DIR + File.separator + "semana_" + semanaNum);
                if (!weekFolder.exists()) {
                    weekFolder.mkdirs();
                }

                File fileToSave = new File(weekFolder, fileName);
                filePart.write(fileToSave.getAbsolutePath());
            }
        }

        response.sendRedirect("PortafolioServlet?semana=" + semanaNum);
    }

    private boolean isImageFile(String fileName) {
        String lower = fileName.toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png") || lower.endsWith(".gif") || lower.endsWith(".webp");
    }

    private boolean isPdfFile(String fileName) {
        String lower = fileName.toLowerCase();
        return lower.endsWith(".pdf");
    }

    private String getSubmittedFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1);
            }
        }
        return null;
    }
}